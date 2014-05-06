package activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import audio.ERSAudioPlayer;
import view.GameView;

public class GameActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GameView gView = new GameView(this);
        gView.setKeepScreenOn(true);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(gView);
        ERSAudioPlayer.playBGM();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ERSAudioPlayer.playBGM();
    }
    @Override
    protected void onPause() {
        super.onPause();
        ERSAudioPlayer.stopBGM();
    }
}