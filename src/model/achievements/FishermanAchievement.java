package model.achievements;

import model.GameStatistics;
import model.Model;

public abstract class FishermanAchievement extends PassiveAchievement {
    private final int limit;

    public FishermanAchievement(String name, int limit) {
        super(name + " Fisherman", "You caught " + limit + " fish.");
        this.limit = limit;
    }

    @Override
    public boolean condition(Model model) {
        return GameStatistics.getFishCaught() >= limit;
    }
}
