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
        SoundJLayer sjl = new SoundJLayer(getSoundResource(key, false));
        sjl.play();
    }

    private static ClientSound getSoundResource(String key, boolean background) {
        if (!loadedSounds.keySet().contains(key)) {
            loadSoundResource(key, background);
        }
        return loadedSounds.get(key);
    }

    public static void loadSoundResource(String key, boolean lowVolume) {
        System.out.println("Resources " + key + " isn't loaded, loading it");
        byte[] bytes = SoundManager.decodeAsBase64(SoundManager.getSoundAsBase64(key));
        loadedSounds.put(key, new ClientSound(bytes, lowVolume));
    }


    public static void playSoundsInSuccession(String[] split) {
        List<ClientSound> byteList = new ArrayList<>();
        for (String s : split) {
            System.out.println("Sound to play: " + s);
            byteList.add(getSoundResource(s, false));
        }
        if (effectsSoundQueue == null || !effectsSoundQueue.isPlaying()) {
            System.out.println("No sound effect going on, making new one");
            effectsSoundQueue = new SoundJLayer(byteList);
            effectsSoundQueue.play();
        } else {
            System.out.println("Already playing something, adding to queue");
            effectsSoundQueue.addToQueue(byteList);
        }

    }

    private static synchronized void playBackgroundSound(String ambientSound) {
        if (!backgroundSound.equals(ambientSound)) {
            System.out.println("Old bg song is " + backgroundSound + ", new is " + ambientSound);
            backgroundSound = ambientSound;
            if (bgSoundLayer != null) {
                bgSoundLayer.stop();
            }
            if (!ambientSound.equals("nothing")) {
                bgSoundLayer = new SoundJLayer(getSoundResource(ambientSound, true), true);
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
