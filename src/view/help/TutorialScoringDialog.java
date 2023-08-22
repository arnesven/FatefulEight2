package view.help;

import model.Model;
import view.GameView;

public class TutorialScoringDialog extends HelpDialog {

    private static final String TEXT =
            "When you retire (after achieving a Party reputation of " + Model.REP_TO_WIN + ", " +
            "or after 100 days) you're game will be scored. Your score is calculated in the following way:\n\n" +
            "Reputation Points......1000 each\n" +
            "Days Remaining...........10 each\n" +
            "Party Members.........20-30 each\n" +
            "Gold and Equipment...total value\n" +
            "Spells..........25 per different\n";

    public TutorialScoringDialog(GameView previous) {
        super(previous, "Scoring", TEXT);
    }
}
