package model.achievements;

import model.GameStatistics;
import model.Model;

public class DiligentStudentAchievement extends PassiveAchievement {
    public DiligentStudentAchievement() {
        super("Diligent Student", "You attended 25 training sessions.");
    }

    @Override
    public boolean condition(Model model) {
        return GameStatistics.getTrainingSessions() >= 25;
    }
}
