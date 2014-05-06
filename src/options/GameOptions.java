package options;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class GameOptions {
    private static SharedPreferences prefs;
    private static boolean SFX = true;

    public static void init(Context context) {
        prefs = context.getSharedPreferences("ERS_Prefs", Context.MODE_PRIVATE);
        SFX = prefs.getBoolean("sfx", true);
    }

    public static void setSFX(boolean b) {
        Editor e  = prefs.edit();
        e.putBoolean("sfx", b);
        e.commit();
        SFX = b;
    }

    public static boolean getSFX() {
        return SFX;
    }
}
