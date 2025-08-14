package model.achievements;

public class AlucardAchievement extends Achievement {

    public static final String KEY = AlucardAchievement.class.getCanonicalName();

    public AlucardAchievement() {
        super(new Achievement.Data(KEY,
                "Alucard", "You killed a vampire with a character who was also a vampire."));
    }
}
