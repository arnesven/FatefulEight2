package sound;

public enum BackgroundMusic {
    mainSong("1_country_road"),
    combatSong("2_clash_alert"),
    citySong("3_fast_city"),
    mysticSong("5_mystic_ruin"),
    calmingSong("calming_walk"),
    clinkBeatSong("clink_beat"),
    endingSong("ending"),
    endingAltSong("ending2"),
    festiveSong("festive"),
    festiveAltSong("festive2"),
    horseRacingSong("horse_racing"),
    jumpyBlip("jumpy_blip"),
    seriousSong("serious"),
    seriousAltSong("serious_alt");

    public static final float VOLUME = -15.0f;
    private final String fileName;

    BackgroundMusic(String filename) {
        this.fileName = filename;
    }

    public String getFileName() {
        return fileName;
    }
}
