package model.achievements;

import model.GameStatistics;
import model.Model;

public class RitualistAchievement extends PassiveAchievement {
    public RitualistAchievement() {
        super("Ritualist", "You participated in 15 Rituals.");
    }

    @Override
    public boolean condition(Model model) {
        return GameStatistics.getRituals() >= 15;
    }
}
