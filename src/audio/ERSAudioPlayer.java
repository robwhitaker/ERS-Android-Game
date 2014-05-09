package audio;

import android.content.Context;
import android.media.SoundPool;
import android.media.AudioManager;

import com.example.ers.R;
import options.GameOptions;

import java.util.HashMap;

public class ERSAudioPlayer {
    private static SoundPool soundPool;
    private static HashMap<String, Integer> registeredSounds;
    private static Context playerContext;

    public static void init(Context context) {
        soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC,100);
        registeredSounds = new HashMap<String, Integer>(3);
        playerContext = context;

        registeredSounds.put("cardDown", soundPool.load(playerContext, R.raw.card_down,2));
        registeredSounds.put("slap", soundPool.load(playerContext, R.raw.deck_slap,3));
    }

    public static void playSFX(String id) {
        if(!((Boolean) GameOptions.get("sfx")))
            return;
        float volume = 1.0f;
        if(soundPool == null || registeredSounds == null)
            init(playerContext);
        else
            soundPool.play(registeredSounds.get(id),volume,volume,1,0,1f);
    }
}
