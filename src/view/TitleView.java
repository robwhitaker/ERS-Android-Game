package view;

import activity.MainActivity;
import com.example.ers.R;

import audio.ERSAudioPlayer;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.view.MotionEvent;
import android.view.View;
import activity.GameActivity;

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

    public TitleView(Context context) {
        super(context);
        currentContext = context;

        ERSAudioPlayer.init(currentContext);

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
                    Intent gameIntent = new Intent(currentContext, GameActivity.class);
                    currentContext.startActivity(gameIntent);
                    ERSAudioPlayer.playSFX("cardDown");
                }
                if(optionButtonPressed) {
                    ERSAudioPlayer.playSFX("cardDown");
//                    Intent gameIntent = new Intent(currentContext,GameActivity.class);
//                    currentContext.startActivity(gameIntent);
                }
                playButtonPressed = false;
                optionButtonPressed = false;
                break;
        }

        invalidate();
        return true;
    }
}