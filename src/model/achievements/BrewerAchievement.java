package model.achievements;

import model.GameStatistics;
import model.Model;

public class BrewerAchievement extends PassiveAchievement {
    private final int limit;

    public BrewerAchievement(String name, int limit) {
        super(name + " Brewer", "You brewed or distilled " + limit + " potions.");
        this.limit = limit;
    }

    @Override
    public boolean condition(Model model) {
        return GameStatistics.getPotionsBrewed() + GameStatistics.getPotionsDistilled() >= limit;
    }
}
