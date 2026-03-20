package model.achievements;

public class HighDamageAchievement extends Achievement {
    private static final int MAX_DAMAGE = 20;
    public static final String KEY = HighDamageAchievement.class.getCanonicalName();

    public HighDamageAchievement() {
        super(new Data(KEY, "Dynamo", "You dealt " + MAX_DAMAGE + " damage in a single attack."));
    }

    public static boolean qualifyForCompletion(int damage) {
        return damage >= MAX_DAMAGE;
    }
}
