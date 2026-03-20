package model.achievements;

import model.GameStatistics;
import model.Model;

public class DuelistAchievement extends PassiveAchievement {
    public DuelistAchievement() {
        super("Duelist", "You competed in 10 Magic Duels");
    }

    @Override
    public boolean condition(Model model) {
        return GameStatistics.getMagicDuels() >= 10;
    }
}
