package model.achievements;

import model.GameStatistics;
import model.Model;

public class EnvoyOfDeathAchievement extends PassiveAchievement {
    private static final int COUNT_REQUIRED = 3;

    public EnvoyOfDeathAchievement() {
        super("Envoy of Death", "You've carried out " + COUNT_REQUIRED + " assassinations.");
    }

    @Override
    public boolean condition(Model model) {
        return GameStatistics.getAssassinations() >= COUNT_REQUIRED;
    }
}
