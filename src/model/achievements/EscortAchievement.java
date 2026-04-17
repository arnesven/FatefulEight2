package model.achievements;

import model.GameStatistics;
import model.Model;

public class EscortAchievement extends PassiveAchievement {
    private static final int COUNT = 6;

    public EscortAchievement() {
        super("Escort", "You've escorted " + COUNT + " travellers to their destination.");
    }

    @Override
    public boolean condition(Model model) {
        return GameStatistics.getTravellers() >= COUNT;
    }
}
