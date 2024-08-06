package sound;

import model.Party;

import java.util.*;

public class ClientSoundManager extends SoundManager {

    private static Map<String, ClientSound> loadedSounds = new HashMap<>();
    private static SoundJLayer effectsSoundQueue;
    private static String backgroundSound = "nothing";
    private static SoundJLayer bgSoundLayer;
    private static List<BackgroundMusic> bgSoundStack = new ArrayList<>();

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

    private static synchronized void playBackgroundSound(BackgroundMusic ambientSound) {
        if (!backgroundSound.equals(ambientSound.getFileName())) {
            System.out.println("Old bg song is " + backgroundSound + ", new is " + ambientSound);
            bgSoundStack.add(0, ambientSound);
            backgroundSound = ambientSound.getFileName();
            if (bgSoundLayer != null) {
                bgSoundLayer.stop();
            }
            if (!ambientSound.getFileName().equals("nothing")) {
                bgSoundLayer = new SoundJLayer(getSoundResource(ambientSound.getFileName(), Volume.LOWEST), true);
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
        playBackgroundSound(song);
    }

    public synchronized static void playPreviousBackgroundMusic() {
        if (!bgSoundStack.isEmpty()) {
            BackgroundMusic last = bgSoundStack.remove(0);
            playBackgroundMusic(last);
        } else {
            playBackgroundMusic(BackgroundMusic.mainSong);
        }
    }
}
