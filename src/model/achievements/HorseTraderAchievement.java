package model.achievements;

import model.GameStatistics;
import model.Model;

public class HorseTraderAchievement extends PassiveAchievement {
    public HorseTraderAchievement() {
        super("Horse Trader", "You bought or sold 50 horses.");
    }

    @Override
    public boolean condition(Model model) {
        return GameStatistics.getHorsesBought() + GameStatistics.getHorsesSold() >= 50;
    }
}
