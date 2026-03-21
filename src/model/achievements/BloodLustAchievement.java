package model.achievements;

import model.GameStatistics;
import model.Model;

public class BloodLustAchievement extends PassiveAchievement {
    public BloodLustAchievement() {
        super("Blood Lust", "A vampire in your party sucked the blood of another creature 10 times.");
    }

    @Override
    public boolean condition(Model model) {
        return GameStatistics.getVampireFeedings() >= 10;
    }
}
