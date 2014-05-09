package activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ToggleButton;
import com.example.ers.R;
import options.GameOptions;

public class OptionsActivity extends Activity {

    private ToggleButton SFX;
    private SeekBar turnDelay, pickupDelay, slapDelay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //set up the OptionsView
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.options);

        addOptionListeners(); //add change listeners to option elements
        setDefaultOptions(); //set defaults for options elements
    }

    /**
     * Set option elements to default values based on either what the user has
     * previously set or the defaults defined in GameOptions.
     */
    private void setDefaultOptions() {
        Integer tDelay = (Integer) GameOptions.get("turnDelay");
        turnDelay.setProgress((tDelay != null) ? tDelay:GameOptions.DEFAULT_TURN_DELAY);
        GameOptions.set("turnDelay",turnDelay.getProgress());

        Integer pDelay = (Integer) GameOptions.get("pickupDelay");
        pickupDelay.setProgress((pDelay != null) ? pDelay:GameOptions.DEFAULT_PICKUP_DELAY);
        GameOptions.set("pickupDelay",pickupDelay.getProgress());

        Integer sDelay = (Integer) GameOptions.get("slapDelay");
        slapDelay.setProgress((sDelay != null) ? sDelay:GameOptions.DEFAULT_SLAP_DELAY);
        GameOptions.set("slapDelay",slapDelay.getProgress());

        Boolean sfx = (Boolean) GameOptions.get("sfx");
        SFX.setChecked((sfx != null) ? sfx:GameOptions.DEFAULT_SFX_STATE);
        GameOptions.set("sfx", SFX.isChecked());
    }

    /**
     * Add listeners to option elements that will set the corresponding value in GameOptions.
     */
    private void addOptionListeners() {
        SFX = (ToggleButton) findViewById(R.id.sfxButton);
        SFX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameOptions.set("sfx", SFX.isChecked());
            }
        });

        turnDelay = (SeekBar) findViewById(R.id.compTurnDelaySeek);
        pickupDelay = (SeekBar) findViewById(R.id.compPickupDelaySeek);
        slapDelay = (SeekBar) findViewById(R.id.compSlapDelaySeek);

        turnDelay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView tv = ((TextView) findViewById(R.id.compTurnDelayLabel));
                tv.setText(R.string.turnDelay);
                tv.setText(tv.getText() + ": " + turnDelay.getProgress() + "ms");
                GameOptions.set("turnDelay", turnDelay.getProgress());
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        pickupDelay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView tv = ((TextView) findViewById(R.id.compPickupDelayLabel));
                tv.setText(R.string.pickupDelay);
                tv.setText(tv.getText() + ": " + pickupDelay.getProgress() + "ms");
                GameOptions.set("pickupDelay", pickupDelay.getProgress());
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        slapDelay.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                TextView tv = ((TextView) findViewById(R.id.compSlapDelayLabel));
                tv.setText(R.string.slapDelay);
                tv.setText(tv.getText() + ": " + slapDelay.getProgress() + "ms");
                GameOptions.set("slapDelay", slapDelay.getProgress());
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
