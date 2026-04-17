package model.achievements;

import model.GameStatistics;
import model.Model;

public class BountyHunterAchievement extends PassiveAchievement {
    private static final int COUNT = 3;

    public BountyHunterAchievement() {
        super("Bounty Hunter", "You've completed " + COUNT + " bounty hunts.");
    }

    @Override
    public boolean condition(Model model) {
        return GameStatistics.getBountyHunts() >= COUNT;
    }
}
