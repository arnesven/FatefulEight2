package model.achievements;

import model.GameStatistics;
import model.Model;

public class OrcSlayerAchievement extends PassiveAchievement {
    public OrcSlayerAchievement() {
        super("Orc Slayer", "You've killed 100 orcs.");
    }

    @Override
    public boolean condition(Model model) {
        return GameStatistics.getOrcsKilled() >= 100;
    }
}
