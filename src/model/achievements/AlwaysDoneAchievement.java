package model.achievements;

import model.Model;

public class AlwaysDoneAchievement extends Achievement {

    public AlwaysDoneAchievement() {
        super(new Data("", "Always Done", "Completed from the start"));
    }

    @Override
    public boolean isCompleted(Model model) {
        return true;
    }
}
