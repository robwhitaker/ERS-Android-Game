package activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import com.example.ers.R;
import options.GameOptions;

public class OptionsActivity extends Activity {

    private CheckBox SFX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.options);

        addSFXListener();
    }

    private void addSFXListener() {
        SFX = (CheckBox) findViewById(R.id.sfx);
        SFX.setChecked(GameOptions.getSFX());
        SFX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameOptions.setSFX(SFX.isChecked());
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
