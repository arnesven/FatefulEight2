package sound;

public enum BackgroundMusic {
    mainSong("1_country_road"),
    combatSong("2_clash_alert"),
    citySong("3_fast_city"),
    mysticSong("5_mystic_ruin");

    public static final float VOLUME = -15.0f;
    private final String fileName;

    BackgroundMusic(String filename) {
        this.fileName = filename;
    }

    public String getFileName() {
        return fileName;
    }
}
