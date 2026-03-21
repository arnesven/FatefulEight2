package model.achievements;

import model.Model;

import java.util.Map;

public abstract class PassiveAchievement extends Achievement {
    private boolean announced = false;

    public PassiveAchievement(String name, String description) {
        super(new Achievement.Data("", name, description));
    }

    public abstract boolean condition(Model model);

    @Override
    public final boolean isCompleted(Model model) {
        return condition(model);
    }

    public void registerYourself(Map<String, Achievement> partyAchievements) {
        partyAchievements.put(getName(), this);
    }

    @Override
    public boolean isCompletionAnnounced() {
        return announced;
    }

    public void setAnnounced(boolean announced) {
        this.announced = announced;
    }
}
