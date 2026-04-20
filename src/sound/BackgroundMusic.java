package sound;

public enum BackgroundMusic {
    mainSong("1_country_road", Volume.VOL_1),
    combatSong("2_clash_alert", Volume.VOL_1),
    citySong("3_fast_city", Volume.VOL_1),
    mysticSong("5_mystic_ruin", Volume.VOL_1),
    calmingSong("calming_walk", Volume.VOL_8),
    strongholdSong("clink_beat", Volume.VOL_8),
    castleSong("ending", Volume.VOL_8),
    endingSong("ending2", Volume.VOL_8),
    festiveSong("festive", Volume.VOL_8),
    lightQuestSong("festive2", Volume.VOL_8),
    horseRacingSong("horse_racing", Volume.VOL_8),
    jumpyBlip("jumpy_blip", Volume.VOL_3),
    battleSong("serious", Volume.VOL_8),
    altCombatSong("retro_rush", Volume.VOL_8),
    dungeonSong("deepest_thought", Volume.VOL_10),
    gentleMemory("gentle_memory", Volume.VOL_8),
    ridingSong("going_home", Volume.VOL_8),
    happyMandolin("happy_mandolin", Volume.VOL_8),
    caveSong("long_ago", Volume.VOL_8),
    templeSong("neverending", Volume.VOL_8),
    darkQuestSong("dark_quest", Volume.VOL_8);

    public static final Volume DEFAULT_VOLUME = Volume.VOL_8;
    private final String fileName;
    private final Volume volume;

    BackgroundMusic(String filename, Volume volume) {
        this.fileName = filename;
        this.volume = volume;
    }

    public String getFileName() {
        return fileName;
    }

    public Volume getVolume() {
        return volume;
    }
}
