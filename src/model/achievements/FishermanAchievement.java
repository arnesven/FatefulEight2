package model.achievements;

import model.GameStatistics;
import model.Model;

public class FishermanAchievement extends PassiveAchievement {
    public FishermanAchievement() {
        super("Fisherman", "You caught 100 fish.");
    }

    @Override
    public boolean condition(Model model) {
        return GameStatistics.getFishCaught() >= 100;
    }
}
