package model.achievements;

import model.GameStatistics;
import model.Model;

public class CardSharkAchievement extends PassiveAchievement {
    public CardSharkAchievement() {
        super("Card Shark", "You played 50 card games.");
    }

    @Override
    public boolean condition(Model model) {
        return GameStatistics.getCardGamesPlayed() >= 50;
    }
}
