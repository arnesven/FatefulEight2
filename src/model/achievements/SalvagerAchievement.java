package model.achievements;

import model.GameStatistics;
import model.Model;

public class SalvagerAchievement extends PassiveAchievement {
    public SalvagerAchievement() {
        super("Salvager", "You salvaged 200 items.");
    }

    @Override
    public boolean condition(Model model) {
        return GameStatistics.getItemsSalvaged() >= 200;
    }
}
