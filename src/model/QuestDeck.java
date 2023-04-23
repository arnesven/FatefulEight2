package model;

import model.map.HexLocation;
import model.quests.*;
import util.MyRandom;

import java.io.Serializable;
import java.util.*;

public class QuestDeck extends ArrayList<Quest> implements Serializable {

    private static final List<Quest> QUESTS = makeAllQuests();

    private final Set<String> acceptedQuests = new HashSet<>();
    private final Set<String> questLocations = new HashSet<>();

    public Quest getRandomQuest() {
        return MyRandom.sample(QUESTS);
    }


    private static List<Quest> makeAllQuests() {
        return List.of(
                new DeepDungeonQuest(),
                new MansionHeistQuest(),
                new UnsuspectingLoversQuest(),
                new MissingBrotherQuest(),
                new DefendTheVillageQuest(),
                new RatProblemQuest(),
                new TreasureHuntQuest(),
                new SurveillanceQuest(),
                new WizardsTowerQuest(),
                new HauntedMansionQuest(),
                new ArenaQuest(),
                new TownFairQuest()
                // new AbandonedMineQuest(), // Hard
                // new MasqueradeQuest(), // medium
                // new WarlocksDungeonQuest(), // Easy
                // new MurderMysteryQuest(), // Hard
                // new CultistDenQuest(), // medium
                // new ForestTrollQuest(), // medium
                // new GoblinTunnelsQuest(), // medium
                // new ElvenHighCouncilQuest(), // hard


        );
    }

    public void accept(Quest quest, HexLocation location) {
        questLocations.add(location.getName());
        acceptedQuests.add(quest.getName());
    }

    public boolean alreadyDone(HexLocation location) {
        return questLocations.contains(location.getName());
    }

    public boolean alreadyDone(Quest quest) {
        return acceptedQuests.contains(quest.getName());
    }
}
