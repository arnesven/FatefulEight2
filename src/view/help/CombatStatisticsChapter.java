package view.help;

import view.GameView;

public class CombatStatisticsChapter extends SubChapterHelpDialog {

    private static final String TEXT =
            "In the Combat Summary View, you will find some interesting statistics compiled from " +
            "data collected during combat.\n\n" +
            "Accuracy - The percentage of your attacks which dealt at least 1 damage.\n\n" +
            "MVP - Most Valued Party Member\n" +
            "The character who killed the most enemies during combat.";

    public CombatStatisticsChapter(GameView view) {
        super(view, "Statistics", TEXT);
    }
}
