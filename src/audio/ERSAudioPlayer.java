package audio;

import android.content.Context;
import android.media.SoundPool;
import android.media.AudioManager;

import com.example.ers.R;
import options.GameOptions;

import java.util.HashMap;

/**
 * ERSAudioPlayer class handles loading and playing audio throughout the application.
 */
public class ERSAudioPlayer {
    private static SoundPool soundPool;
    private static HashMap<String, Integer> registeredSounds;
    private static Context playerContext;

    /**
     * Initializes the SoundPool and the registeredSounds HashMap. Adds SoundPool references
     * to HashMap for easy access.
     * @param context The context used in the SoundPool
     */
    public static void init(Context context) {
        soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC,100);
        registeredSounds = new HashMap<String, Integer>(3);
        playerContext = context;

        registeredSounds.put("cardDown", soundPool.load(playerContext, R.raw.card_down,2));
        registeredSounds.put("slap", soundPool.load(playerContext, R.raw.deck_slap,3));
    }

    /**
     * Plays a registered sound effect given the corresponding key
     * @param id The key used to reference the sound effect
     */
    public static void playSFX(String id) {
        //if SoundEffects are not enabled, return before playing one
        Boolean sfx = (Boolean) GameOptions.get("sfx");
        sfx         = (sfx != null) ? sfx:GameOptions.DEFAULT_SFX_STATE;
        if(!sfx)
            return;

        //play sound
        float volume = 1.0f;
        if(soundPool == null || registeredSounds == null)
            init(playerContext);
        else
            soundPool.play(registeredSounds.get(id),volume,volume,1,0,1f);
    }
}
