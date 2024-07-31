package view.help;

import model.headquarters.Headquarters;
import model.states.dailyaction.HeadquartersDailyActionState;
import view.GameView;

public class TutorialAssignments extends SubChapterHelpDialog {
    private static final String TEXT = "If your leader has at least " +
            HeadquartersDailyActionState.LEADERSHIP_REQUIRED_RANKS_FOR_ASSIGNMENTS +
            " ranks of Leadership, you can set assignments for characters who are staying at headquarters.\n\n" +
            "R'n'R: The character simply relaxes at headquarters.\n\n" +
            "Shopping: The character will spend gold from headquarters to restock it with food if below the limit.\n\n" +
            "Town Work: The character will try to make gold in town. " +
            "This expends 1-2 Stamina for the character and can generate up to " + Headquarters.MAX_GOLD_FROM_WORK +
            " x number of characters gold each day.";

    public TutorialAssignments(GameView view) {
        super(view, "Assignments", TEXT);
    }
}
