package model.achievements;

import model.Model;
import model.QuestDeck;
import model.enemies.VampireEnemy;
import model.mainstory.MainStory;
import model.map.*;
import model.quests.Quest;
import model.states.events.*;
import util.MyLists;

import java.io.Serializable;
import java.util.*;

public class GameAchievements implements Serializable {
    private final Map<String, Achievement> partyAchievements;

    public GameAchievements() {
        partyAchievements = new HashMap<>();
        addEventAchievements();
        addQuestAchievements();
        addDungeonAchievements();
        addMiscAchievements();

        printAchievements();
    }

    private void printAchievements() {
        for (Achievement a : partyAchievements.values()) {
            System.out.println(a.getKey() + ":" + a.getName());
        }

    }

    private void addMiscAchievements() {
        new LittleSpenderAchievement().registerYourself(partyAchievements);
        new BigSpenderAchievement().registerYourself(partyAchievements);
        new LittleEarnerAchievement().registerYourself(partyAchievements);
        new BigEarnerAchievement().registerYourself(partyAchievements);
        new FishermanAchievement().registerYourself(partyAchievements);
        new OrcSlayerAchievement().registerYourself(partyAchievements);
        partyAchievements.put(AlucardAchievement.KEY, new AlucardAchievement());
    }

    private void addDungeonAchievements() {
        World w = new World(WorldBuilder.buildWorld(WorldType.original), WorldBuilder.getWorldBounds(WorldBuilder.ORIGINAL),
                WorldType.original);

        for (UrbanLocation urb : w.getLordLocations()) {
            if (urb instanceof CastleLocation) {
                registerAchievement(((CastleLocation) urb).getDungeonAchievement());
            }
        }

        for (RuinsLocation loc : w.getRuinsLocations()) {
            registerAchievement(loc.getAchievementData());
        }

        for (TombLocation loc : w.getTombLocations()) {
            registerAchievement(loc.getAchievementData());
        }
    }

    private void addQuestAchievements() {
        List<Quest> allQuests = new ArrayList<>(QuestDeck.getAllQuests());
        allQuests.addAll(MainStory.getQuests());
        for (Quest q : allQuests) {
            if (q.givesAchievement()) {
                registerAchievement(q.getAchievementData());
            }
        }
    }

    private void addEventAchievements() {
        registerAchievement(BurningBuildingEvent.getAchievementData());
        registerAchievement(DiggingGameEvent.getAchievementData());
        registerAchievement(DwarvenCityEvent.getAchievementData());
        registerAchievement(EnchantressEvent.getAchievementData());
        registerAchievement(GardenMazeEvent.getAchievementData());
        registerAchievement(SmugglersEvent.getAchievementData());
        registerAchievement(TallSpireEvent.getAchievementData());
        registerAchievement(VisitMonasteryEvent.getAchievmentData());
        for (Achievement.Data data : CommandOutpostDailyEventState.getAchievementDatas()) {
            registerAchievement(data);
        }
    }

    private void registerAchievement(Achievement.Data achievementData) {
        partyAchievements.put(achievementData.getKey(), new Achievement(achievementData));
    }

    public int numberOfCompleted(Model model) {
        return MyLists.intAccumulate(getAsList(), a -> a.isCompleted(model) ? 1 : 0);
    }

    public int getTotal() {
        return partyAchievements.size();
    }

    public List<Achievement> getAsList() {
        List<Achievement> result = new ArrayList<>(partyAchievements.values());
        result.sort(Comparator.comparing(Achievement::getName));
        return result;
    }

    public void setCompleted(String key) {
        if (!partyAchievements.containsKey(key)) {
            for (Achievement a : partyAchievements.values()) {
                System.err.println(a.getKey() + ":" + a.getName());
            }

            throw new IllegalArgumentException("No achievement for '" + key + "'");
        }
        partyAchievements.get(key).setCompleted(true);
    }

    public boolean isCompleted(String key, Model model) {
        return partyAchievements.get(key).isCompleted(model);
    }

    public Achievement getAchievement(String key) {
        return partyAchievements.get(key);
    }
}
