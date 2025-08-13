package model.achievements;

import model.GameStatistics;
import model.Model;

public class LittleSpenderAchievement extends Achievement {

    public LittleSpenderAchievement() {
        super(new Data("", "Little Spender", "You've spent 100 gold."));
    }

    @Override
    public boolean isCompleted(Model model) {
        return GameStatistics.getGoldLost() >= 100;
    }
}
