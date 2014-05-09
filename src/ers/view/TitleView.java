package ers.view;

import ers.activity.OptionsActivity;
import com.example.ers.R;

import ers.audio.ERSAudioPlayer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import ers.activity.GameActivity;
import ers.options.GameOptions;

/**
 * Title screen view
 */
public class TitleView extends View {
    private Bitmap titleGraphic;
    private int screenW, screenH;
    private Bitmap playButtonUp;
    private Bitmap playButtonDown;
    private Bitmap optionButtonUp;
    private Bitmap optionButtonDown;
    private boolean playButtonPressed;
    private boolean optionButtonPressed;
    private Context currentContext;

    /**
     * Default constructor. Initializes game.
     * @param context the current context
     */
    public TitleView(Context context) {
        super(context);
        currentContext = context;

        //initialize audio player and game options
        ERSAudioPlayer.init(currentContext);
        GameOptions.init(context);

        titleGraphic = BitmapFactory.decodeResource(getResources(), R.drawable.title_graphic);
        playButtonUp = BitmapFactory.decodeResource(getResources(), R.drawable.play_button_up);
        playButtonDown = BitmapFactory.decodeResource(getResources(), R.drawable.play_button_down);

        optionButtonUp = BitmapFactory.decodeResource(getResources(), R.drawable.option_button_up);
        optionButtonDown = BitmapFactory.decodeResource(getResources(), R.drawable.option_button_down);
    }

    @Override
    public void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        screenW = w;
        screenH = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //draw the logo and buttons to the screen
        Bitmap playButton = playButtonPressed ? playButtonDown:playButtonUp;
        Bitmap optionButton = optionButtonPressed ? optionButtonDown:optionButtonUp;
        canvas.drawBitmap(titleGraphic, (screenW-titleGraphic.getWidth())/2,0,null);
        canvas.drawBitmap(playButton,
                (screenW-playButton.getWidth())/2,
                (int) (screenH*0.7),
                null);
        canvas.drawBitmap(optionButton,
                (screenW-optionButton.getWidth())/2,
                (int) (screenH*0.85),
                null);
    }

    public boolean onTouchEvent(MotionEvent event) {
        int eventAction = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();

        //check if the user has pressed either of the buttons
        switch(eventAction) {
            case MotionEvent.ACTION_DOWN:
                if (x > (screenW-playButtonUp.getWidth())/2 &&
                        (x < ((screenW-playButtonUp.getWidth())/2) +
                                playButtonUp.getWidth()) &&
                        y > (int)(screenH*0.7) &&
                        y < (int)(screenH*0.7) +
                                playButtonUp.getHeight()) {
                    playButtonPressed = true;
                }
                if (x > (screenW-optionButtonUp.getWidth())/2 &&
                        (x < ((screenW-optionButtonUp.getWidth())/2) +
                                optionButtonUp.getWidth()) &&
                        y > (int)(screenH*0.85) &&
                        y < (int)(screenH*0.85) +
                                optionButtonUp.getHeight()) {
                    optionButtonPressed = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                if(playButtonPressed) {
                    //if the play button was pressed, go to the game activity
                    Intent gameIntent = new Intent(currentContext, GameActivity.class);
                    currentContext.startActivity(gameIntent);
                    ERSAudioPlayer.playSFX("cardDown");
                }
                if(optionButtonPressed) {
                    //if the option button was pressed, go to the options activity
                    ERSAudioPlayer.playSFX("cardDown");
                    Intent gameIntent = new Intent(currentContext, OptionsActivity.class);
                    currentContext.startActivity(gameIntent);
                }
                //reset playButtonPressed and optionButtonPressed
                playButtonPressed = false;
                optionButtonPressed = false;
                break;
        }

        invalidate();
        return true;
    }
}