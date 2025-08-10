package model.achievements;

import model.states.events.*;
import util.MyLists;

import java.io.Serializable;
import java.util.*;

public class GameAchievements implements Serializable {
    private final Map<String, Achievement> partyAchievements;

    public GameAchievements() {
        partyAchievements = new HashMap<>();
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

    public int numberOfCompleted() {
        return MyLists.intAccumulate(getAsList(), a -> a.isCompleted() ? 1 : 0);
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
        partyAchievements.get(key).setCompleted(true);
    }

    public boolean isCompleted(String key) {
        return partyAchievements.get(key).isCompleted();
    }
}
