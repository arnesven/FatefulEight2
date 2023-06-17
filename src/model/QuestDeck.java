package model;

import model.map.HexLocation;
import model.quests.*;
import util.MyPair;
import util.MyRandom;

import java.io.Serializable;
import java.util.*;

public class QuestDeck implements Serializable {

    private static final List<Quest> QUESTS = makeAllQuests();

    private final Set<LocationAndQuest> locationsAndQuests = new HashSet<>();
    private final Set<String> acceptedQuests = new HashSet<>();
    private final Set<String> questLocations = new HashSet<>();
    private final Map<String, Boolean> flagLocations = new HashMap<>();

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
                new TownFairQuest(),
                new AbandonedMineQuest(),
                new MasqueradeQuest(),
                new WarlocksDungeonQuest(),
                new MurderMysteryQuest(),
                new CultistDenQuest(),
                new ForestTrollQuest(),
                new GoblinTunnelsQuest(),
                new ElvenHighCouncilQuest()
        );
    }

    public void accept(Quest quest, HexLocation location, int day) {
        locationsAndQuests.add(new LocationAndQuest(location.getName(), quest.getName(), day));
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

    public boolean hasFlagIn(HexLocation location) {
        return flagLocations.containsKey(location.getName());
    }

    public void setSuccessfulIn(HexLocation location) {
        flagLocations.put(location.getName(), true);
    }

    public void setFailureIn(HexLocation location) {
        flagLocations.put(location.getName(), false);
    }

    public void unsetFailureIn(HexLocation location) {
        flagLocations.remove(location.getName());
        flagLocations.remove(location.getName());
    }

    public Set<LocationAndQuest> getLocationsAndQuests() {
        return locationsAndQuests;
    }

    public static List<Quest> getAllQuests() {
        return QUESTS;
    }

    public static class LocationAndQuest extends MyPair<String, String> {
        private int day;

        public LocationAndQuest(String location, String quest, int day) {
            super(location, quest);
            this.day = day;
        }

        public String getLocation() {
            return first;
        }

        public String getQuest() {
            return second;
        }

        public int getDay() {
            return day;
        }
    }
}
