package sound;

public enum BackgroundMusic {
    mainSong("1_country_road", Volume.LOW_BGM),
    combatSong("2_clash_alert", Volume.LOW_BGM),
    citySong("3_fast_city", Volume.LOW_BGM),
    mysticSong("5_mystic_ruin", Volume.LOW_BGM),
    calmingSong("calming_walk", Volume.MEDIUM_BGM),
    strongholdSong("clink_beat", Volume.MEDIUM_BGM),
    castleSong("ending", Volume.MEDIUM_BGM),
    endingSong("ending2", Volume.MEDIUM_BGM),
    festiveSong("festive", Volume.MEDIUM_BGM),
    lightQuestSong("festive2", Volume.MEDIUM_BGM),
    horseRacingSong("horse_racing", Volume.MEDIUM_BGM),
    jumpyBlip("jumpy_blip", Volume.LOW),
    battleSong("serious", Volume.MEDIUM_BGM),
    altCombatSong("retro_rush", Volume.MEDIUM_BGM),
    dungeonSong("deepest_thought", Volume.VERY_HIGH_BGM),
    gentleMemory("gentle_memory", Volume.MEDIUM_BGM),
    ridingSong("going_home", Volume.MEDIUM_BGM),
    happyMandolin("happy_mandolin", Volume.MEDIUM_BGM),
    caveSong("long_ago", Volume.MEDIUM_BGM),
    templeSong("neverending", Volume.MEDIUM_BGM),
    darkQuestSong("dark_quest", Volume.MEDIUM_BGM);

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
