package model.achievements;

import model.GameStatistics;
import model.Model;

public class SailorAchievement extends PassiveAchievement {
    public SailorAchievement() {
        super("Sailor", "You've taken 10 chartered boat trips.");
    }

    @Override
    public boolean condition(Model model) {
        return GameStatistics.boatsChartered() >= 10;
    }
}
