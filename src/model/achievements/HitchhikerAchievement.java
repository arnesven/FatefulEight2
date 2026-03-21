package model.achievements;

import model.GameStatistics;
import model.Model;

public class HitchhikerAchievement extends PassiveAchievement {
    public HitchhikerAchievement() {
        super("Hitchhiker", "You've traveled (not on foot) 10 times for free!");
    }

    @Override
    public boolean condition(Model model) {
        return GameStatistics.getFreeRides() >= 10;
    }
}
