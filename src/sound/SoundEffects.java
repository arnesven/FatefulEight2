package sound;

import model.SettingsManager;

public class SoundEffects {

    public static final Volume DEFAULT_VOLUME = Volume.VOL_6;
    private static SettingsManager settings = null;

    private static void play(String key) {
        playWithVolume(key, DEFAULT_VOLUME);
    }

    private static void playWithVolume(String key, Volume vol) {
        ClientSoundManager.playSoundWithVolume(key, modifyBySettings(vol));
    }

    private static Volume modifyBySettings(Volume vol) {
        if (settings == null) {
            return vol;
        }
        int diff = vol.ordinal() - DEFAULT_VOLUME.ordinal();
        int newIndex = Math.min(Volume.values().length-1, Math.max(settings.getEffectsSoundLevel().ordinal() + diff, 0));
        return Volume.values()[newIndex];
    }
    
    public static Volume modifyBySettingsBg(Volume vol) {
        if (settings == null) {
            return vol;
        }
        int diff = vol.ordinal() - BackgroundMusic.DEFAULT_VOLUME.ordinal();
        int newIndex = Math.min(Volume.values().length-1, Math.max(settings.getMusicSoundLevel().ordinal() + diff, 0));
        return Volume.values()[newIndex];
    }

    public static void menuDown() {
        play("menu_down");
    }

    public static void menuUp() {
        play("menu_up");
    }

    public static void menuSelect() {
        play("menu_select");
    }

    public static void menuQuit() {
        play("menu_quit");
    }

    public static void matrixSelect() {
        play("matrix_select");
    }

    public static void arrowMenu() { play("arrow_menu");}

    public static void playSound(String sound) {
        if (!sound.equals("")) {
            play(sound);
        }
    }

    public static void sellItem() {
        play("coin");
    }

    public static void gameLoaded() {
        play("load");
    }

    public static void gameSaved() {
        playWithVolume("save", Volume.VOL_3);
    }

    public static void preload(String key, boolean low) {
        ClientSoundManager.loadSoundResource(key, low ? Volume.VOL_4 : Volume.VOL_6);
    }

    public static void playBoom() {
        play("boom");
    }

    public static void playUnlock() {
        play("unlock");
    }

    public static void playSpellSuccess() { play("spell_suc"); }

    public static void playSpellFail() { play("spell_fail"); }

    public static void playMiss() {
        play("miss");
    }

    public static void playGrass() {
        play("grass");
    }

    public static void playTargetHit() {
        play("target_hit");
    }

    public static void playHitWood() {
        play("wood");
    }

    public static void failedSkill() {
        play("failure");
    }

    public static void successSkill() {
        playWithVolume("success", Volume.VOL_5);
    }

    public static void playBamf() {
        playWithVolume("bamf", Volume.VOL_7);
    }

    public static void digging() {
        playWithVolume("digging", Volume.VOL_7);
    }

    public static void playFoundBoulder() {
        playWithVolume("found_boulder", Volume.VOL_3);
    }

    public static void playClickSound() {
        playWithVolume("click", Volume.VOL_6);
    }

    public static void playRockBreak() {
        playWithVolume("rockbreak", Volume.VOL_7);
    }

    public static void playElevator() {
        playWithVolume("elevator", Volume.VOL_6);
    }

    public static void setSettings(SettingsManager gameDataSettings) {
        settings = gameDataSettings;
    }
}
