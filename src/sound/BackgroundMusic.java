package sound;

public enum BackgroundMusic {
    mainSong("1_country_road", Volume.LOW_BGM),
    combatSong("2_clash_alert", Volume.LOW_BGM),
    citySong("3_fast_city", Volume.LOW_BGM),
    mysticSong("5_mystic_ruin", Volume.LOW_BGM),
    calmingSong("calming_walk", Volume.LOW),
    strongholdSong("clink_beat", Volume.LOW),
    castleSong("ending", Volume.LOW),
    endingSong("ending2", Volume.LOW),
    festiveSong("festive", Volume.HIGHEST),
    lightQuestSong("festive2", Volume.LOW),
    horseRacingSong("horse_racing", Volume.LOW),
    jumpyBlip("jumpy_blip", Volume.LOW),
    battleSong("serious", Volume.LOW),
    altCombatSong("retro_rush", Volume.LOW),
    dungeonSong("deepest_thought", Volume.LOW),
    gentleMemory("gentle_memory", Volume.LOW),
    ridingSong("going_home", Volume.LOW),
    happyMandolin("happy_mandolin", Volume.LOW),
    caveSong("long_ago", Volume.LOW),
    templeSong("neverending", Volume.LOW),
    darkQuestSong("dark_quest", Volume.LOW);

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
