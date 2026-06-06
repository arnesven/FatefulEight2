package model;

import model.quests.*;
import util.MyRandom;

import java.util.*;

public class QuestDeck {

    private static final List<Quest> QUESTS = makeAllQuests();

    private static List<Quest> makeAllQuests() {
        return List.of(
                new AbandonedMineQuest(),     // QI
                new ArenaQuest(),             // QI
                new BrrbitsReward(),          // QI
                new CultistDenQuest(),        // QI
                new DeepDungeonQuest(),       // QI
                new DefendTheVillageQuest(),  // QI
                new ElvenHighCouncilQuest(),  // QI
                new GoblinTunnelsQuest(),     // QI
                new HauntedMansionQuest(),    // QI
                new HungryTrollQuest(),       // QI
                new MagicSeminarQuest(),      // QI
                new MansionHeistQuest(),      // QI
                new MasqueradeQuest(),        // QI
                new MissingBrotherQuest(),    // QI
                new MurderMysteryQuest(),     // QI
                new OrcishDelightQuest(),     // QI
                new RatProblemQuest(),        // QI
                new SurveillanceQuest(),      // QI
                new SwampOgreQuest(),         // QI
                new TownFairQuest(),
                new TreasureHuntQuest(),
                new UnsuspectingLoversQuest(),
                new WarlocksDungeonQuest(),    // QI
                new WerewolfQuest(),
                new WizardsTowerQuest()
                // new WineTastingQuest()
        );
    }

    public Quest getRandomQuest() {
        return MyRandom.sample(QUESTS);
    }

    public static List<Quest> getAllQuests() {
        return QUESTS;
    }

    public Quest getQuestByName(String key) {
        for (Quest q : QUESTS) {
            if (q.getName().equals(key)) {
                return q;
            }
        }
        throw new IllegalStateException("No such quest found: " + key);
    }
}
