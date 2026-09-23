package sound;

import model.Model;
import model.journal.PartSevenStoryPart;
import model.journal.PartSixStoryPart;
import util.MyLists;
import util.MyRandom;

import java.util.List;

public enum BackgroundMusic {
    mainSong("1_country_road", Volume.VOL_1), // Start of game, on load
    combatSong("2_clash_alert", Volume.VOL_1), // Combat
    citySong("3_fast_city", Volume.VOL_1), // Towns, Some TownishLocations
    mysticSong("5_mystic_ruin", Volume.VOL_1), // Swamps, Vampire Prowl, Some Quests
    calmingSong("calming_walk", Volume.VOL_8), // Two Quests (Missing Brother, Treasure Hunt)
    strongholdSong("clink_beat", Volume.VOL_8), // Two Quests (Ancient Stronghold, Mind Machine)
    castleSong("ending", Volume.VOL_8), // Castles
    endingSong("ending2", Volume.VOL_8), // End of Game song
    festiveSong("festive", Volume.VOL_8), // Tavern, HeadQuarters, Avert Mutiny Quest
    lightQuestSong("festive2", Volume.VOL_8), // Many Quests
    horseRacingSong("horse_racing", Volume.VOL_8), // Horse Race
    jumpyBlip("jumpy_blip", Volume.VOL_3), // Mini-games
    battleSong("serious", Volume.VOL_8), // Battles
    altCombatSong("retro_rush", Volume.VOL_8), // Combat
    dungeonSong("dungeon", Volume.VOL_8), // Dungeons
    gentleMemory("gentle_memory", Volume.VOL_8), // One Quest (Unsuspecting Lovers) and One Event (Boyfriend/Girlfriend)
    ridingSong("going_home", Volume.VOL_8), // Riding
    happyMandolin("happy_mandolin", Volume.VOL_8), // Fly with broom, One Quest (Town Fair), Two Events (Market and Party Entertainment)
    longAgoSong("long_ago", Volume.VOL_8), // Ancient City, Garden Maze, Advanced Mine
    templeSong("neverending", Volume.VOL_8), // Temples
    darkQuestSong("dark_quest", Volume.VOL_8), // Many Quests
    upbeatCombat("upbeat_combat", Volume.VOL_9), // Combat (surprise)
    miniBoss("mini_boss", Volume.VOL_9), // Combat (ambush),
    vampireProwl("vampire_prowl", Volume.VOL_9), // Vampire prowl
    act1Song1("act_1_song_1", Volume.VOL_8), // Travel during Act 1
    act1Song2("act_1_song_2", Volume.VOL_8), // Travel during Act 1
    act1Song3("act_1_song_3", Volume.VOL_8), // Travel during Act 1
    act2Song("act_2_song", Volume.VOL_9), // Travel during Act 2
    act3Song("act_3_song", Volume.VOL_9), // Travel during Act 3
    caveSong("caves", Volume.VOL_9), // Caves
    ritualSong("magic_ritual", Volume.VOL_9), // Rituals
    wasteland("wasteland", Volume.VOL_9), //
    griefSong("grief_song", Volume.VOL_8), // Grief
    creditsSong("credits", Volume.VOL_8); // Credits

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

    public static BackgroundMusic getMainSong(Model model) {
        if (MyLists.any(model.getMainStory().getStoryParts(), sp -> sp instanceof PartSevenStoryPart)) {
            return BackgroundMusic.act3Song;
        }
        if (MyLists.any(model.getMainStory().getStoryParts(), sp -> sp instanceof PartSixStoryPart)) {
            return BackgroundMusic.act2Song;
        }
        return MyRandom.sample(List.of(BackgroundMusic.act1Song1, BackgroundMusic.act1Song2, BackgroundMusic.act1Song2));
    }
}
