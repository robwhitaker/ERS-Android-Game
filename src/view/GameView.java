package view;

import audio.ERSAudioPlayer;
import com.example.ers.R;

import deck.Deck;
import deck.Card;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;
import android.app.Activity;
import options.GameOptions;

public class GameView extends View {
    private Bitmap cardBack;
    private int screenW, screenH;
    private int cardScaleX, cardScaleY;
    private Context currentContext;
    private Deck playerDeck, computerDeck, centerPile;
    private Paint whitePaint,blackPaint;

    public static final int PLAYER = 1;
    public static final int COMPUTER = 2;

    private int TURN = PLAYER;
    private int NUM_VALID_SLAP_CARDS = 0;
    private int CHANCES = -1;
    private int PICKUP = -1;

    private boolean deckJustTaken = false;
    public static boolean gameActive = true;

    private Handler handler;

    public GameView(Context context) {
        super(context);
        currentContext = context;
        playerDeck = new Deck();
        computerDeck = new Deck();
        centerPile = new Deck();
        handler = new Handler();

        float scale = context.getResources().getDisplayMetrics().density;
        whitePaint = new Paint();
        whitePaint.setAntiAlias(true);
        whitePaint.setColor(Color.WHITE);
        whitePaint.setStyle(Paint.Style.STROKE);
        whitePaint.setTextAlign(Paint.Align.CENTER);
        whitePaint.setTextSize(scale*25);
        whitePaint.setShadowLayer(1.0f,1.5f,1.5f, Color.BLACK);

        blackPaint = new Paint();
        blackPaint.setAntiAlias(true);
        blackPaint.setColor(Color.BLACK);
        blackPaint.setStyle(Paint.Style.STROKE);
        blackPaint.setTextAlign(Paint.Align.LEFT);
        blackPaint.setTextSize(scale*20);
        blackPaint.setShadowLayer(1.0f,1.5f,1.5f, Color.GRAY);

        TURN = PLAYER;
        NUM_VALID_SLAP_CARDS = 0;
        CHANCES = -1;
        PICKUP = -1;
        gameActive = true;
        deckJustTaken = false;
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenW = w;
        screenH = h;
        cardScaleX = (int) (screenW / 3.5);
        cardScaleY = (int) ((screenW / 3.5) * 1.28);
        initGame();

        Bitmap tempCard = BitmapFactory.decodeResource(currentContext.getResources(), R.drawable.card_back);
        cardBack = Bitmap.createScaledBitmap(tempCard,
                (int) (screenW / 3.5),
                (int) ((screenW / 3.5) * 1.28),
                false);
    }

    private void initGame() {
        //load all the cards into one deck
        Deck tempDeck = new Deck();
        for (int i = 0; i < 4; i++) {
            for (int j = 102; j < 115; j++) {
                int tempId = j + (i*100);
                Card tempCard = new Card(tempId);
                int resourceId = getResources().getIdentifier("card" + tempId, "drawable", currentContext.getPackageName());
                Bitmap tempBitmap = BitmapFactory.decodeResource(currentContext.getResources(), resourceId);
                Bitmap scaledBitmap = Bitmap.createScaledBitmap(tempBitmap, cardScaleX , cardScaleY, false);
                tempCard.setBitmap(scaledBitmap);
                tempDeck.push(tempCard);
            }
        }

        //shuffle the deck
        tempDeck.shuffle();

        //distribute cards between player and computer decks evenly
        for(int i=0; tempDeck.size() > 0; i++)
            ((i%2 == 0) ? playerDeck:computerDeck).push(tempDeck.pop());

        runComputer();
    }

    private void drawGameBoard(Canvas canvas) {
        //DRAW THE PLAYER'S DECK
        switch(playerDeck.size()) {
            case 0: //the player has no cards, so don't draw any
                break;
            case 1: //the player has 1 card, so only draw one
                canvas.drawBitmap(cardBack,
                        (int) ((screenW - cardBack.getWidth())/2),
                        (int) (screenH - cardBack.getHeight()*1.25),
                        null);
                break;
            default: //otherwise, draw 2 to make it look like the player has multiple
                //bottom bottom card
                canvas.drawBitmap(cardBack,
                        (int) ((screenW - cardBack.getWidth()*1.1)/2),
                        (int) (screenH - cardBack.getHeight()*1.20),
                        null);
                //top bottom card
                canvas.drawBitmap(cardBack,
                        (int) ((screenW - cardBack.getWidth())/2),
                        (int) (screenH - cardBack.getHeight()*1.25),
                        null);
                break;
        }
        //write the size of the player's deck
        canvas.drawText(Integer.toString(playerDeck.size()),
                (int) ((screenW - cardBack.getWidth())/2 + cardBack.getWidth()/2),
                (int) (screenH - cardBack.getHeight()*1.25 + cardBack.getHeight()/2)+whitePaint.getTextSize()/2,
                whitePaint);

        //DRAW THE COMPUTER'S DECK
        switch(computerDeck.size()) {
            case 0: //the computer has no cards, so don't draw any
                break;
            case 1: //the computer has 1 card, so only draw one
                canvas.drawBitmap(cardBack,
                        (int) ((screenW - cardBack.getWidth())/2),
                        (int) (cardBack.getHeight()*0.25),
                        null);
                break;
            default: //otherwise, draw 2 to make it look like the computer has multiple
                //top bottom card
                canvas.drawBitmap(cardBack,
                        (int) ((screenW - cardBack.getWidth()*1.1)/2),
                        (int) (cardBack.getHeight()*0.3),
                        null);
                //top top card
                canvas.drawBitmap(cardBack,
                        (int) ((screenW - cardBack.getWidth())/2),
                        (int) (cardBack.getHeight()*0.25),
                        null);
                break;
        }
        //write the size of the computer's deck
        canvas.drawText(Integer.toString(computerDeck.size()),
                (int) (((screenW - cardBack.getWidth())/2) + cardBack.getWidth()/2),
                (int) ((cardBack.getHeight()*0.25) + cardBack.getHeight()/2)+whitePaint.getTextSize()/2,
                whitePaint);

        //DRAW THE CENTER PILE
        Card[] topCards;
        switch(centerPile.size()) {
            case 0: //there is nothing in the center pile, so don't draw anything
                break;
            case 1: //there is only one card in the center pile, so just draw that
                canvas.drawBitmap(centerPile.peek().getBitmap(),
                        (int) (screenW- (cardBack.getWidth()))/2,
                        (int) ((screenH - cardBack.getHeight())*0.5),
                        null);
                break;
            case 2: //there are 2 cards in the center pile, so draw them both
                topCards = centerPile.peek(2);
                canvas.drawBitmap(topCards[1].getBitmap(),
                        (int) (screenW- (cardBack.getWidth()))/2,
                        (int) ((screenH - cardBack.getHeight())*0.5),
                        null);
                canvas.drawBitmap(topCards[0].getBitmap(),
                        (int) (screenW- (cardBack.getWidth()*0.6))/2,
                        (int) ((screenH - cardBack.getHeight())*0.48),
                        null);
                break;
            case 3: //there are 3 cards in the center pile, so draw them all
                topCards = centerPile.peek(3);
                canvas.drawBitmap(topCards[2].getBitmap(),
                        (int) (screenW- (cardBack.getWidth()))/2,
                        (int) ((screenH - cardBack.getHeight())*0.5),
                        null);
                canvas.drawBitmap(topCards[1].getBitmap(),
                        (int) (screenW- (cardBack.getWidth()*0.6))/2,
                        (int) ((screenH - cardBack.getHeight())*0.48),
                        null);
                canvas.drawBitmap(topCards[0].getBitmap(),
                        (int) (screenW- (cardBack.getWidth()*0.2))/2,
                        (int) ((screenH - cardBack.getHeight())*0.46),
                        null);
                break;
            case 4: //there are 4 cards...
                topCards = centerPile.peek(4);
                canvas.drawBitmap(topCards[3].getBitmap(),
                        (int) (screenW- (cardBack.getWidth()*1.1))/2,
                        (int) ((screenH - cardBack.getHeight())*0.51),
                        null);
                //visible top cards
                canvas.drawBitmap(topCards[2].getBitmap(),
                        (int) (screenW- (cardBack.getWidth()))/2,
                        (int) ((screenH - cardBack.getHeight())*0.5),
                        null);
                canvas.drawBitmap(topCards[1].getBitmap(),
                        (int) (screenW- (cardBack.getWidth()*0.6))/2,
                        (int) ((screenH - cardBack.getHeight())*0.48),
                        null);
                canvas.drawBitmap(topCards[0].getBitmap(),
                        (int) (screenW- (cardBack.getWidth()*0.2))/2,
                        (int) ((screenH - cardBack.getHeight())*0.46),
                        null);
                break;
            default: //there are more than 3 cards, so draw a kind of full looking deck
                topCards = centerPile.peek(5);
                //decorative cards
                canvas.drawBitmap(topCards[4].getBitmap(),
                        (int) (screenW- (cardBack.getWidth()*1.2))/2,
                        (int) ((screenH - cardBack.getHeight())*0.52),
                        null);
                canvas.drawBitmap(topCards[3].getBitmap(),
                        (int) (screenW- (cardBack.getWidth()*1.1))/2,
                        (int) ((screenH - cardBack.getHeight())*0.51),
                        null);
                //visible top cards
                canvas.drawBitmap(topCards[2].getBitmap(),
                        (int) (screenW- (cardBack.getWidth()))/2,
                        (int) ((screenH - cardBack.getHeight())*0.5),
                        null);
                canvas.drawBitmap(topCards[1].getBitmap(),
                        (int) (screenW- (cardBack.getWidth()*0.6))/2,
                        (int) ((screenH - cardBack.getHeight())*0.48),
                        null);
                canvas.drawBitmap(topCards[0].getBitmap(),
                        (int) (screenW- (cardBack.getWidth()*0.2))/2,
                        (int) ((screenH - cardBack.getHeight())*0.46),
                        null);
                break;
        }

        //draw turn text
        float turnTextY = (TURN == PLAYER) ? (int) (screenH - cardBack.getHeight()*1.25 + cardBack.getHeight()/2)+blackPaint.getTextSize()/2:
                (int) ((cardBack.getHeight()*0.25) + cardBack.getHeight()/2)+blackPaint.getTextSize()/2;
        float turnTextX =(float) (screenW*0.02);
        String turnText = (TURN == PLAYER) ? "Player":"Computer";

        canvas.drawText(turnText+"'s", turnTextX, turnTextY-blackPaint.getTextSize()/2, blackPaint);
        canvas.drawText("turn.", turnTextX, turnTextY+blackPaint.getTextSize()/2, blackPaint);

        //draw chances text
        if(CHANCES > 0) {
            float chancesTextY = screenH/2;
            canvas.drawText("Chances: "+CHANCES, turnTextX, chancesTextY, blackPaint);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        drawGameBoard(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int)event.getX();
        int y = (int)event.getY();

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //if the player taps his own deck
                if(x > (screenW-cardBack.getWidth())/2 &&
                        (x < ((screenW-cardBack.getWidth())/2) +
                                cardBack.getWidth()) &&
                        y > (int) (screenH - cardBack.getHeight()*1.25) &&
                        y < (int) (screenH - cardBack.getHeight()*1.25) +
                                cardBack.getHeight() &&
                        TURN == PLAYER) { //and it's the player's turn
                    placeCard(PLAYER);
                }

                //if the player taps the center pile
                if(x > (screenW-cardBack.getWidth()*1.2)/2 &&
                        (x < ((screenW-cardBack.getWidth()*0.2)/2) +
                                cardBack.getWidth()) &&
                        y > (int) ((screenH - cardBack.getHeight())*0.46) &&
                        y < (int) (int) ((screenH - cardBack.getHeight())*0.52) +
                                cardBack.getHeight()) {
                    if(PICKUP != PLAYER)
                        handleSlap(PLAYER);
                    else
                        pickUpDeck(PLAYER);
                }
                break;
        }
        invalidate();
        return true;
    }

    @SuppressLint("NewApi")
    private void runComputer() {
        Integer pDelay = (Integer) GameOptions.get("pickupDelay");
        final Integer pDelay_ = (pDelay != null) ? pDelay:GameOptions.DEFAULT_PICKUP_DELAY;

        Integer sDelay = (Integer) GameOptions.get("slapDelay");
        final Integer sDelay_ = (sDelay != null) ? sDelay:GameOptions.DEFAULT_SLAP_DELAY;

        Integer tDelay = (Integer) GameOptions.get("turnDelay");
        final Integer tDelay_ = (tDelay != null) ? tDelay:GameOptions.DEFAULT_TURN_DELAY;

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                while(gameActive) {
                    //check every 100ms for changes
                    try {Thread.sleep(100);} catch (InterruptedException e){}
                    if(PICKUP == COMPUTER) {
                        try {Thread.sleep(pDelay_); } catch (InterruptedException e) {}
                        handler.post(new Runnable() {
                            public void run() {
                                if(PICKUP == COMPUTER)
                                    pickUpDeck(COMPUTER);
                            }
                        });
                    } else if(isSlappable()) {
                        try {Thread.sleep(sDelay_);} catch (InterruptedException e){}
                        if(isSlappable())
                            handler.post(new Runnable() {
                                public void run() {
                                    if(isSlappable());
                                    handleSlap(COMPUTER);
                                }
                            });
                    } else if(TURN == COMPUTER) {
                        try {Thread.sleep(tDelay_);} catch (InterruptedException e){}

                        if(TURN==COMPUTER)
                            placeCard(COMPUTER);
                    }
                }
            }
        });
    }

    private void asyncInvalidate() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                invalidate();
            }
        });
    }

    private void asyncPlaySound(String soundID) {
        class RunWithParam implements Runnable {
            private String sId;
            public RunWithParam(String id) {
                sId = id;
            }
            public void run() {
                ERSAudioPlayer.playSFX(sId);
            }
        }

        handler.post(new RunWithParam(soundID));
    }

    private void pickUpDeck(int receiver) {
        Deck deckHandle = (receiver == PLAYER) ? playerDeck:computerDeck;
        String receiverName = (receiver == PLAYER) ? "Player":"Computer";

        Toast.makeText(currentContext, receiverName+" takes the deck!",
                Toast.LENGTH_SHORT).show();
        deckHandle.addToBottom(centerPile);
        NUM_VALID_SLAP_CARDS = 0;
        TURN = receiver;
        PICKUP = -1;
        CHANCES = -1;
        deckJustTaken = true;
        //switch deckJustTaken off in 500 milliseconds
        handler.postDelayed(new Runnable() {
            public void run() {
                deckJustTaken = false;
            }
        }, 500);
        asyncPlaySound("slap");
        asyncInvalidate();
    }

    private boolean placeCard(int placer) {
        Deck deckHandle = (placer == PLAYER) ? playerDeck:computerDeck;
//		String placerName = (placer == PLAYER) ? "Player":"Computer";


        //if the placer has no more cards to place, end the game here
        if(deckHandle.size() < 1) {
            //if there is a possible move left
            if(isSlappable() || PICKUP == placer) {
                Toast.makeText(currentContext, "No cards left!",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
            gameActive = false;
            //otherwise, the game is over
            //class to open Dialog with params in thread
            class RunWithParam implements Runnable {
                private int placer;
                public RunWithParam(int p) {placer=p;}
                public void run() {
                    String opponentName = (placer == PLAYER) ? "Computer":"Player";
                    AlertDialog.Builder builder = new AlertDialog.Builder(currentContext);
                    builder.setTitle(opponentName + " wins!");
                    builder.setMessage("Click below to exit the match.");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Exit",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    ((Activity) currentContext).finish();
                                    dialog.cancel();
                                }
                            });

                    builder.create().show();
                }
            }
            //open dialog
            handler.post(new RunWithParam(placer));

            return false;
        }

        if(PICKUP == -1 && CHANCES == -1) {
            Card c = deckHandle.pop();
            centerPile.push(c);
            NUM_VALID_SLAP_CARDS++;
            switch(c.getRank()) {
                case Card.JACK:
                    CHANCES = 1;
                    break;
                case Card.QUEEN:
                    CHANCES = 2;
                    break;
                case Card.KING:
                    CHANCES = 3;
                    break;
                case Card.ACE:
                    CHANCES = 4;
                    break;
            }
            TURN = (TURN == PLAYER) ? COMPUTER:PLAYER; //switch the turn
            asyncPlaySound("cardDown");
            asyncInvalidate();
            return true;
        }

        if(CHANCES > 0) {
            Card c = deckHandle.pop();
            centerPile.push(c);
            NUM_VALID_SLAP_CARDS++;
            switch(c.getRank()) {
                case Card.JACK:
                    CHANCES = 1;
                    TURN = (TURN == PLAYER) ? COMPUTER:PLAYER; //switch the turn
                    break;
                case Card.QUEEN:
                    CHANCES = 2;
                    TURN = (TURN == PLAYER) ? COMPUTER:PLAYER; //switch the turn
                    break;
                case Card.KING:
                    CHANCES = 3;
                    TURN = (TURN == PLAYER) ? COMPUTER:PLAYER; //switch the turn
                    break;
                case Card.ACE:
                    CHANCES = 4;
                    TURN = (TURN == PLAYER) ? COMPUTER:PLAYER; //switch the turn
                    break;
                default:
                    if(CHANCES-1 == 0) {
                        PICKUP = (placer == PLAYER) ? COMPUTER:PLAYER;
                        CHANCES = -1;
                        asyncPlaySound("cardDown");
                        asyncInvalidate();
                        return true;
                    }
                    CHANCES--;
            }
            asyncPlaySound("cardDown");
            asyncInvalidate();
            return true;
        }
        return false;

    }

    private boolean isSlappable() {
        Card[] cards;
        switch(centerPile.size()) {
            case 0:
            case 1:
                //not enough cards to slap. instantly wrong. misslap.
                return false;
            case 2:
                //enough cards to slap, but not enough to check 3
                //if not enough valid slap cards, it's still a misslap
                if(NUM_VALID_SLAP_CARDS != 2)
                    break;

                cards = centerPile.peek(2);
                //if the two cards match (double / snap)
                if(cards[0].getRank() == cards[1].getRank())
                    return true;
                break;
            default: //3 or more cards
                cards = centerPile.peek(3);
                //if the top two cards match or the top and bottom cards match (sandwich)
                //given that enough cards are valid to slap
                if((NUM_VALID_SLAP_CARDS > 1 && cards[0].getRank() == cards[1].getRank()) ||
                        (NUM_VALID_SLAP_CARDS > 2 && cards[0].getRank() == cards[2].getRank()))
                    return true;
        }
        //guess it wasn't slappable. shame.
        return false;
    }

    private boolean handleSlap(int slapper) {
        if(deckJustTaken) //if the deck was just taken, don't count this slap
            return false;
        Deck deckHandle = (slapper == PLAYER) ? playerDeck:computerDeck;
        String slapperName = (slapper == PLAYER) ? "Player":"Computer";
        Card[] cards;
        asyncPlaySound("slap");
        switch(centerPile.size()) {
            case 0:
            case 1:
                //not enough cards to slap. instantly wrong. misslap.
                break;
            case 2:
                //enough cards to slap, but not enough to check 3
                //if not enough valid slap cards, it's still a misslap
                if(NUM_VALID_SLAP_CARDS != 2)
                    break;

                cards = centerPile.peek(2);
                //if the two cards match (double / snap)
                if(cards[0].getRank() == cards[1].getRank()) {
                    pickUpDeck(slapper);
                    return true;
                }
                break;
            default: //3 or more cards
                cards = centerPile.peek(3);
                //if the top two cards match or the top and bottom cards match (sandwich)
                //given that enough cards are valid to slap
                if((NUM_VALID_SLAP_CARDS > 1 && cards[0].getRank() == cards[1].getRank()) ||
                        (NUM_VALID_SLAP_CARDS > 2 && cards[0].getRank() == cards[2].getRank())) {
                    pickUpDeck(slapper);
                    return true;
                }
        }
        if(isSlappable()) {
            pickUpDeck(slapper);
            return true;
        } else {
            Toast.makeText(currentContext, "Misslap. "+slapperName+" puts a card under.",
                    Toast.LENGTH_SHORT).show();
            if(deckHandle.size() > 0)
                centerPile.addToBottom(deckHandle.pop());
            return false;
        }
    }

}