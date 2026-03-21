package model.achievements;

public class RenovatorAchievement extends Achievement {
    public static final String KEY = RenovatorAchievement.class.getCanonicalName();

    public RenovatorAchievement() {
        super(new Data(KEY, "Renovator", "You expanded your Headquarters."));
    }
}
