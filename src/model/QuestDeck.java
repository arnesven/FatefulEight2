package model;

import model.map.HexLocation;
import model.quests.*;
import util.MyRandom;

import java.io.Serializable;
import java.util.*;

public class QuestDeck extends ArrayList<Quest> implements Serializable {

    private final Set<String> acceptedQuests = new HashSet<>();
    private final Set<String> questLocations = new HashSet<>();

    public Quest getRandomQuest() {
        return MyRandom.sample(List.of(
                new DeepDungeonQuest(),
                new MansionHeistQuest(),
                new UnsuspectingLoversQuest(),
                new MissingBrotherQuest(),
                new DefendTheVillageQuest(),
                new RatProblemQuest(),
                new TreasureHuntQuest(),
                new SurveillanceQuest(),
                new WizardsTowerQuest()
        ));
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
