package model;

import model.quests.*;
import util.MyRandom;

import java.util.*;

public class QuestDeck {

    private static final List<Quest> QUESTS = makeAllQuests();

    private static List<Quest> makeAllQuests() {
        return List.of(
                new AbandonedMineQuest(),
                new ArenaQuest(),
                new BrrbitsReward(),
                new CultistDenQuest(),
                new DeepDungeonQuest(),
                new DefendTheVillageQuest(),
                new ElvenHighCouncilQuest(),
                new GoblinTunnelsQuest(),
                new HauntedMansionQuest(),
                new HungryTrollQuest(),
                new MagicSeminarQuest(),
                new MansionHeistQuest(),
                new MasqueradeQuest(),
                new MissingBrotherQuest(),
                new MurderMysteryQuest(),
                new OrcishDelightQuest(),
                new RatProblemQuest(),         // QI
                new SurveillanceQuest(),
                new SwampOgreQuest(),
                new TownFairQuest(),
                new TreasureHuntQuest(),
                new UnsuspectingLoversQuest(),
                new WarlocksDungeonQuest(),
                new WerewolfQuest(),
                new WizardsTowerQuest()
        );
    }

    public Quest getRandomQuest() {
        return new AbandonedMineQuest();//MyRandom.sample(QUESTS);
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
