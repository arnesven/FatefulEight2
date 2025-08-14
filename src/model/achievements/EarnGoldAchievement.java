package model.achievements;

import model.GameStatistics;
import model.Model;

public class EarnGoldAchievement extends PassiveAchievement {
    private final int amount;

    public EarnGoldAchievement(int amount, String name) {
        super(name, "You've earned " + amount + " gold.");
        this.amount = amount;
    }

    @Override
    public boolean condition(Model model) {
        return GameStatistics.getGoldEarned() >= amount;
    }
}
