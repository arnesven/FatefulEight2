package model.achievements;

import model.GameStatistics;
import model.Model;

public class HorsemanAchievement extends PassiveAchievement {
    public HorsemanAchievement() {
        super("Horseman", "You participated in 20 horse races.");
    }

    @Override
    public boolean condition(Model model) {
        return GameStatistics.getHorseRaces() >= 20;
    }
}
