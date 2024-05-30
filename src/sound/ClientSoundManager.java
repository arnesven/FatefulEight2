package sound;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientSoundManager extends SoundManager {

    private static Map<String, ClientSound> loadedSounds = new HashMap<>();
    private static SoundJLayer effectsSoundQueue;
    private static String backgroundSound = "nothing";
    private static SoundJLayer bgSoundLayer;

    public static void playSound(String key) {
        SoundJLayer sjl = new SoundJLayer(getSoundResource(key, Volume.HIGH));
        sjl.play();
    }

    public static void playSoundWithVolume(String key, Volume vol) {
        SoundJLayer sjl = new SoundJLayer(getSoundResource(key, vol));
        sjl.play();
    }

    private static ClientSound getSoundResource(String key, Volume vol) {
        if (!loadedSounds.keySet().contains(key)) {
            loadSoundResource(key, vol);
        }
        return loadedSounds.get(key);
    }

    public static void loadSoundResource(String key, Volume vol) {
        System.out.println("Resources " + key + " isn't loaded, loading it");
        byte[] bytes = SoundManager.decodeAsBase64(SoundManager.getSoundAsBase64(key));
        loadedSounds.put(key, new ClientSound(bytes, vol.amplitude));
    }

    private static synchronized void playBackgroundSound(String ambientSound) {
        if (!backgroundSound.equals(ambientSound)) {
            System.out.println("Old bg song is " + backgroundSound + ", new is " + ambientSound);
            backgroundSound = ambientSound;
            if (bgSoundLayer != null) {
                bgSoundLayer.stop();
            }
            if (!ambientSound.equals("nothing")) {
                bgSoundLayer = new SoundJLayer(getSoundResource(ambientSound, Volume.LOWEST), true);
                bgSoundLayer.play();
            }
        }
    }

    public synchronized static void stopPlayingBackgroundSound() {
        if (bgSoundLayer != null) {
            bgSoundLayer.stop();
            System.out.println("STOPPING AMBIENT SOUND");
        }
    }

    public synchronized static void playBackgroundMusic(BackgroundMusic song) {
        //stopPlayingBackgroundSound();
        playBackgroundSound(song.getFileName());
    }
}
