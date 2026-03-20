package model.achievements;

public class MultiSlayerAchievement extends Achievement {

    public static final String KEY = MultiSlayerAchievement.class.getCanonicalName();

    public MultiSlayerAchievement() {
        super(new Data(KEY, "Multi-Slayer", "A member of your party killed 10 enemies in a single combat."));
    }

    public static boolean qualifyForCompletion(int killsFor) {
        return killsFor >= 10;
    }
}
