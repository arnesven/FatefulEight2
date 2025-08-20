package model.achievements;

public class DungeonAchievement extends Achievement {
    public DungeonAchievement(Data achievementData) {
        super(achievementData);
    }

    @Override
    public String getType() {
        return "Dungeon";
    }
}
