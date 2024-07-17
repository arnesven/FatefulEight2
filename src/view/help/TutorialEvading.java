package view.help;

import view.GameView;

public class TutorialEvading extends SubChapterHelpDialog {
    private static final String TEXT =
            "Each time a character is attacked they have a chance to evade that attack. " +
            "The chance to evade is based on the difference in speed between the character " +
            "and the attacking enemy. For each 2 points of difference in speed, the character " +
            "gets 1 Evade point (minimum of 0). If the roll of a D10 is not a 10 and less than or equal to the number of evade " +
            "points, the attack is evaded and does no damage to the character " +
            "(it does not even hit them).";

    public TutorialEvading(GameView view) {
        super(view, "Evading", TEXT);
    }
}
