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
    private final Map<String, Boolean> flagLocations = new HashMap<>();

    public Quest getRandomQuest() {
        return MyRandom.sample(QUESTS);
    }


    private static List<Quest> makeAllQuests() {
        return List.of(
//                new DeepDungeonQuest(),
//                new MansionHeistQuest(),
//                new UnsuspectingLoversQuest(),
//                new MissingBrotherQuest(),
//                new DefendTheVillageQuest(),
//                new RatProblemQuest(),
//                new TreasureHuntQuest(),
//                new SurveillanceQuest(),
                new WizardsTowerQuest(),
                new HauntedMansionQuest()
//                new ArenaQuest(),
//                new TownFairQuest(),
//                new AbandonedMineQuest(),
//                new MasqueradeQuest(),
//                new WarlocksDungeonQuest(),
//                new MurderMysteryQuest(),
//                new CultistDenQuest(),
//                new ForestTrollQuest(),
//                new GoblinTunnelsQuest(),
//                new ElvenHighCouncilQuest()
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

    public boolean wasSuccessfulIn(HexLocation location) {
        return flagLocations.get(location.getName());
    }

    public void setSuccessfulIn(HexLocation location) {
        flagLocations.put(location.getName(), true);
    }

    public void setFailureIn(HexLocation location) {
        flagLocations.put(location.getName(), false);
    }
}
