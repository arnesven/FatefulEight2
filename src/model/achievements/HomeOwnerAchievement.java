package model.achievements;

public class HomeOwnerAchievement extends Achievement {
    public static final String KEY = HomeOwnerAchievement.class.getCanonicalName();

    public HomeOwnerAchievement() {
        super(new Data(KEY, "Home Owner", "You have procured a house which works as headquarters for your adventuring company."));
    }

    @Override
    public String getDescription() {
        return super.getDescription();
    }
}
