package model;

import model.map.HexLocation;
import model.map.UrbanLocation;
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
    private final Map<String, Boolean> successLocations = new HashMap<>();

    public Quest getRandomQuest() {
        return MyRandom.sample(QUESTS);
    }


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
                new RatProblemQuest(),
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

    public boolean wasSuccess(Model model, Quest quest) {
        Boolean succ = model.getSettings().getMiscFlags().get("QUEST_SUCCESS-"+quest.getName());
        if (succ == null) {
            return false;
        }
        return succ;
    }

    public void setSuccessfulIn(Model model, Quest quest, HexLocation location) {
        model.getSettings().getMiscFlags().put("QUEST_SUCCESS-" + quest.getName(), true);
        successLocations.put(location.getName(), true);
    }

    public void setFailure(Model model, Quest quest) {
        model.getSettings().getMiscFlags().put("QUEST_SUCCESS-" + quest.getName(), false);
    }

    public boolean wasFailure(Model model, Quest quest) {
        Boolean succ = model.getSettings().getMiscFlags().get("QUEST_SUCCESS-"+quest.getName());
        if (succ == null) {
            return false;
        }
        return !succ;
    }

    public void unsetFailureIn(HexLocation location) {
        //flagLocations.remove(location.getName());
        acceptedQuests.remove(location.getName());
        questLocations.remove(location.getName());
        locationsAndQuests.removeIf((LocationAndQuest loc) -> loc.getLocation().equals(location.getName()));
    }

    public Set<LocationAndQuest> getLocationsAndQuests() {
        return locationsAndQuests;
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

    public boolean hadSuccessIn(HexLocation location) {
        return successLocations.containsKey(location.getName());
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
