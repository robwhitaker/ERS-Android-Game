package options;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class GameOptions {
    private static SharedPreferences prefs;
    public static final int     DEFAULT_SLAP_DELAY   = 750;
    public static final int     DEFAULT_PICKUP_DELAY = 600;
    public static final int     DEFAULT_TURN_DELAY   = 400;
    public static final boolean DEFAULT_SFX_STATE = true;

    public static void init(Context context) {
        prefs = context.getSharedPreferences("ERS_Prefs", Context.MODE_PRIVATE);
    }

    public static void set(String key, boolean val) {
        Editor e  = prefs.edit();
        e.putBoolean(key, val);
        e.commit();
    }

    public static void set(String key, int val) {
        Editor e  = prefs.edit();
        e.putInt(key, val);
        e.commit();
    }

    public static Object get(String key) {
        return prefs.contains(key) ? prefs.getAll().get(key):null;
    }
}
