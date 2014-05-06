package audio;

import android.content.Context;
import android.media.SoundPool;
import android.media.AudioManager;
import android.media.MediaPlayer;
import com.example.ers.R;

import java.util.HashMap;

public class ERSAudioPlayer {
    private static SoundPool soundPool;
    private static HashMap<String, Integer> registeredSounds;
    private static MediaPlayer mediaPlayer;
    private static Context playerContext;

    public static void init(Context context) {
        soundPool = new SoundPool(3, AudioManager.STREAM_MUSIC,100);
        registeredSounds = new HashMap(3);
        playerContext = context;

        mediaPlayer = MediaPlayer.create(playerContext, R.raw.cd_battle_theme_2);
        mediaPlayer.setVolume(0.3f,0.3f);
        mediaPlayer.setLooping(true);

        registeredSounds.put("cardDown", soundPool.load(playerContext, R.raw.card_down,2));
        registeredSounds.put("slap", soundPool.load(playerContext, R.raw.deck_slap,3));
    }

    public static void playSFX(String id) {
        float volume = 1.0f;
        if(soundPool == null || registeredSounds == null)
            init(playerContext);
        else
            soundPool.play(registeredSounds.get(id),volume,volume,1,0,1f);
    }

    public static void playBGM() {
        mediaPlayer.start();
    }

    public static void stopBGM() {
        if(mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
            mediaPlayer.reset();
        }
    }
}
