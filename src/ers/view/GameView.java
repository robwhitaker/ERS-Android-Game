package ers.view;

import com.example.ers.R;

import ers.deck.Deck;
import ers.deck.Card;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

public class GameView extends View {
	private Bitmap cardBack;
	private int screenW, screenH;
	private int cardScaleX, cardScaleY;
	private Context currentContext;
	private Deck playerDeck, computerDeck, centerPile;
	private Paint whitePaint;
	
	public static final int PLAYER = 1;
	public static final int COMPUTER = 2;
	
	public static int TURN = PLAYER;
	public static int NUM_VALID_SLAP_CARDS = 0;
	
	
	public GameView(Context context) {
		super(context);
		currentContext = context;
		playerDeck = new Deck();
		computerDeck = new Deck();
		centerPile = new Deck();
		
		float scale = context.getResources().getDisplayMetrics().density;
		whitePaint = new Paint();
		whitePaint.setAntiAlias(true);
		whitePaint.setColor(Color.WHITE);
		whitePaint.setStyle(Paint.Style.STROKE);
		whitePaint.setTextAlign(Paint.Align.CENTER);
		whitePaint.setTextSize(scale*25);
		whitePaint.setShadowLayer(1.0f,1.5f,1.5f, Color.BLACK);
		
		TURN = PLAYER;
		NUM_VALID_SLAP_CARDS = 0;
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
		tempDeck.shuffle();
		tempDeck.shuffle();
		
		//distribute cards between player and computer decks evenly
		for(int i=0; tempDeck.size() > 0; i++) 
			((i%2 == 0) ? playerDeck:computerDeck).push(tempDeck.pop());

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
    					TURN == PLAYER && //and it's the player's turn
    					playerDeck.size() > 0) { //and the player still has cards to play
        				
        			centerPile.push(playerDeck.pop()); //push a card to the top of the deck
        			TURN = COMPUTER; //set it to the computer's turn 
        			NUM_VALID_SLAP_CARDS++; //added one more valid slap card to the deck
        		}
        		
        		//if the player taps the center pile
        		if(x > (screenW-cardBack.getWidth()*1.2)/2 &&
    					(x < ((screenW-cardBack.getWidth()*0.2)/2) +
    					cardBack.getWidth()) &&
    					y > (int) ((screenH - cardBack.getHeight())*0.46) &&
    					y < (int) (int) ((screenH - cardBack.getHeight())*0.52) +
    					cardBack.getHeight()) {
        			handleSlap(PLAYER);
        		}
        		break;
        }
        invalidate();
        return true;
	}
	
	private boolean handleSlap(int slapper) {
		Deck deckHandle = (slapper == PLAYER) ? playerDeck:computerDeck;
		String slapperName = (slapper == PLAYER) ? "Player":"Computer";
		Card[] cards;
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
					Toast.makeText(currentContext, slapperName+" takes the deck!", 
							   Toast.LENGTH_SHORT).show();
					deckHandle.addToBottom(centerPile);
					NUM_VALID_SLAP_CARDS = 0;
					return true;
				}
				break;
			default: //3 or more cards
				cards = centerPile.peek(3);
				//if the top two cards match or the top and bottom cards match (sandwich)
				//given that enough cards are valid to slap
				if((NUM_VALID_SLAP_CARDS > 1 && cards[0].getRank() == cards[1].getRank()) ||
				   (NUM_VALID_SLAP_CARDS > 2 && cards[0].getRank() == cards[2].getRank())) {
					Toast.makeText(currentContext, slapperName+" takes the deck!", 
							   Toast.LENGTH_SHORT).show();
					deckHandle.addToBottom(centerPile);
					NUM_VALID_SLAP_CARDS = 0;
					return true;
				}
		}
		//if we've gotten here, it's a misslap
		Toast.makeText(currentContext, "Misslap. "+slapperName+" puts a card under.", 
				   Toast.LENGTH_SHORT).show();
		if(deckHandle.size() > 0)
			centerPile.addToBottom(deckHandle.pop());
		return false;
	}

}
