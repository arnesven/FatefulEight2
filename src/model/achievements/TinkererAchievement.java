package model.achievements;

import model.GameStatistics;
import model.Model;

public class TinkererAchievement extends PassiveAchievement {
    public TinkererAchievement() {
        super("Tinkerer", "You upgraded 50 items.");
    }

    @Override
    public boolean condition(Model model) {
        return GameStatistics.getItemsUpgraded() >= 50;
    }
}
