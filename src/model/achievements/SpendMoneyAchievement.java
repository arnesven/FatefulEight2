package model.achievements;

import model.GameStatistics;
import model.Model;

public class SpendMoneyAchievement extends PassiveAchievement {
    private final int amount;

    public SpendMoneyAchievement(int amount, String name) {
        super(name, "You've spent " + amount + " gold.");
        this.amount = amount;
    }

    @Override
    public boolean condition(Model model) {
        return GameStatistics.getGoldSpent() >= this.amount;
    }
}
