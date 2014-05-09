package ers.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import ers.view.GameView;

public class GameActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //set up the GameView
        super.onCreate(savedInstanceState);
        GameView gView = new GameView(this);
        gView.setKeepScreenOn(true);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(gView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        GameView.gameActive = false; //stop computer thread
        finish();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
}