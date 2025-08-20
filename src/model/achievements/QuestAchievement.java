package model.achievements;

public class QuestAchievement extends Achievement {
    public QuestAchievement(Data achievementData) {
        super(achievementData);
    }

    @Override
    public String getType() {
        return "Quest";
    }
}
