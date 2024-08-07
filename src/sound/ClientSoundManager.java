package sound;

import model.Party;

import java.util.*;

public class ClientSoundManager extends SoundManager {

    private static Map<String, ClientSound> loadedSounds = new HashMap<>();
    private static SoundJLayer effectsSoundQueue;
    private static BackgroundMusic backgroundSound = null;
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
        if (!loadedSounds.containsKey(key)) {
            loadSoundResource(key, vol);
        }
        return loadedSounds.get(key);
    }

    public static void changeSoundVolume(String key, Volume vol) {
        if (loadedSounds.containsKey(key)) {
            loadedSounds.get(key).setVolume(vol.amplitude);
            if (backgroundSound.getFileName().equals(key)) {
                bgSoundLayer.adjustVolume();
            }
        }
    }

    public static void loadSoundResource(String key, Volume vol) {
        System.out.println("Resources " + key + " isn't loaded, loading it");
        byte[] bytes = SoundManager.decodeAsBase64(SoundManager.getSoundAsBase64(key));
        loadedSounds.put(key, new ClientSound(bytes, vol.amplitude));
    }

    private static synchronized void playBackgroundSound(BackgroundMusic ambientSound) {
        if (backgroundSound == null || !backgroundSound.getFileName().equals(ambientSound.getFileName())) {
            System.out.println("Old bg song is " + backgroundSound + ", new is " + ambientSound);
            backgroundSound = ambientSound;
            if (bgSoundLayer != null) {
                bgSoundLayer.stop();
            }
            if (!ambientSound.getFileName().equals("nothing")) {
                bgSoundLayer = new SoundJLayer(getSoundResource(ambientSound.getFileName(),
                        ambientSound.getVolume()), true);
                bgSoundLayer.play();
            }
        } else {
            System.out.println("Song is same as already playing, skipping");
        }
    }

    public synchronized static void stopPlayingBackgroundSound() {
        if (bgSoundLayer != null) {
            bgSoundLayer.stop();
            System.out.println("STOPPING AMBIENT SOUND");
        }
    }

    public synchronized static void playBackgroundMusic(BackgroundMusic song) {
        System.out.println("Playing song " + song.getFileName());
        playBackgroundSound(song);
    }

    public static BackgroundMusic getCurrentBackgroundMusic() {
        return backgroundSound;
    }
}
