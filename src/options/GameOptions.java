package options;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import java.util.HashMap;

public class GameOptions {
    private static SharedPreferences prefs;

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
        return prefs.getAll().get(key);
    }
}
