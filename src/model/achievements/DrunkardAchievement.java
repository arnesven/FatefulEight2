package model.achievements;

import model.GameStatistics;
import model.Model;

public class DrunkardAchievement extends PassiveAchievement {
    public DrunkardAchievement() {
        super("Drunkards", "Your party members have consumed 100 intoxicating beverages.");
    }

    @Override
    public boolean condition(Model model) {
        return GameStatistics.getIntoxicatingBeverages() >= 100;
    }
}
