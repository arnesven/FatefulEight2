package model.achievements;

import model.GameStatistics;
import model.Model;

public class MonsterHunterAchievement extends PassiveAchievement {
    private static final int COUNT_REQUIRED = 5;

    public MonsterHunterAchievement() {
        super("Monster Hunter", "You've completed " + COUNT_REQUIRED + " Monster Hunts.");
    }

    @Override
    public boolean condition(Model model) {
        return GameStatistics.getMonsterHunts() >= COUNT_REQUIRED;
    }
}
