package model.achievements;

import model.GameStatistics;
import model.Model;

public class ThiefAchievement extends PassiveAchievement {
    private final int limit;

    public ThiefAchievement(String name, int limit) {
        super(name + " Thief", "You stole " + limit + " items.");
        this.limit = limit;
    }

    @Override
    public boolean condition(Model model) {
        return GameStatistics.getItemsStolen() >= limit;
    }
}
