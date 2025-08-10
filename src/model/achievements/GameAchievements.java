package model.achievements;

import model.states.events.BurningBuildingEvent;
import model.states.events.DiggingGameEvent;
import model.states.events.EnchantressEvent;
import util.MyLists;

import java.io.Serializable;
import java.util.*;

public class GameAchievements implements Serializable {
    private final Map<String, Achievement> partyAchievements;

    public GameAchievements() {
        partyAchievements = new HashMap<>();
        registerAchievement(BurningBuildingEvent.getAchievementData());
        registerAchievement(DiggingGameEvent.getAchievementData());
        registerAchievement(EnchantressEvent.getAchievementData());
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
}
