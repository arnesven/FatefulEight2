package sound;

public class SoundEffects {

    public static float VOLUME = 0.0f;

    private static void play(String key) {
        ClientSoundManager.playSound(key);
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
        play("save");
    }

    public static void preload(String key, boolean low) {
        ClientSoundManager.loadSoundResource(key, low);
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
        ClientSoundManager.playSoundLowVolume("success");
    }
}
