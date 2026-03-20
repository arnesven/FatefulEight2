package model.achievements;

import model.GameStatistics;
import model.Model;

public class CrafterAchievement extends PassiveAchievement {
    private final int limit;

    public CrafterAchievement(String name, int limit) {
        super(name + " Crafter", "You crafted " + limit + " items.");
        this.limit = limit;
    }

    @Override
    public boolean condition(Model model) {
        return GameStatistics.getItemsCrafted() >= limit;
    }
}
