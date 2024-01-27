package view.help;

import view.GameView;

public class CombatStatisticsChapter extends SubChapterHelpDialog {

    private static final String TEXT =
            "In the Combat Summary View, you will find some interesting statistics compiled from " +
            "data collected during combat.\n\n" +
            "Accuracy - The percentage of your attacks which dealt at least 1 damage.\n\n" +
            "Reduced Damage - The amount of damage which party members or allies did not take " +
            "because of reduction from Armor Points.\n\n" +
            "Avoided Damage - The amount of damage which party members or allies did not take " +
            "because of blocking or evading.\n\n" +
            "Round Par - An estimated number of round in which the combat should have been able to be fought, based on " +
                    "the average damage of party members (and allies) and the total sum of the enemies' health points.\n\n" +
            "MVP - Most Valued Party Member\n" +
            "The character who killed the most enemies during combat.";

    public CombatStatisticsChapter(GameView view) {
        super(view, "Statistics", TEXT);
    }
}
