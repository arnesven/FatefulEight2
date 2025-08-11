package view.help;

import view.GameView;

public class TutorialScoringDialog extends HelpDialog {

    private static final String TEXT =
            "When you retire (after 100 days) your game will be scored. " +
            "Your score is calculated in the following way:\n\n" +
            "Achievements...........1000 each\n" +
            "Main Story Completed........2500\n";

    public TutorialScoringDialog(GameView previous) {
        super(previous, "Scoring", TEXT);
    }
}
