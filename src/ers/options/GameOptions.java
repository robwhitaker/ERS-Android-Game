package ers.options;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * The GameOptions class holds static options and functions to be accessed throughout
 * the application.
 */
public class GameOptions {
    private static SharedPreferences prefs;
    public static final int     DEFAULT_SLAP_DELAY   = 750;
    public static final int     DEFAULT_PICKUP_DELAY = 600;
    public static final int     DEFAULT_TURN_DELAY   = 400;
    public static final boolean DEFAULT_SFX_STATE = true;

    /**
     * Initialize the options by setting up SharedPreferences
     * @param context The context use to get the SharedPreferences
     */
    public static void init(Context context) {
        prefs = context.getSharedPreferences("ERS_Prefs", Context.MODE_PRIVATE);
    }

    /**
     * Set a key-value pair for an option
     * @param key The string used as a key
     * @param val The boolean value
     */
    public static void set(String key, boolean val) {
        Editor e  = prefs.edit();
        e.putBoolean(key, val);
        e.commit();
    }
    /**
     * Set a key-value pair for an option
     * @param key The string used as a key
     * @param val The integer value
     */
    public static void set(String key, int val) {
        Editor e  = prefs.edit();
        e.putInt(key, val);
        e.commit();
    }

    /**
     * Get an object (setting) from SharedPreferences
     * @param key The key used to refer to that option value
     * @return An object if SharedPreferences contains the key, null otherwise
     */
    public static Object get(String key) {
        return prefs.contains(key) ? prefs.getAll().get(key):null;
    }
}
