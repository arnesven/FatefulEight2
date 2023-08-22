package view.help;

import model.Model;
import view.GameView;

public class HalfTimeDialog extends HelpDialog {
    private static final String TEXT =
            "It has been a while since you started your career as an adventurer. " +
            "Remember that your goal is to achieve a Party Reputation of " + Model.REP_TO_WIN + ". " +
            "Now only 50 days remain to achieve that goal.";

    public HalfTimeDialog(GameView view) {
        super(view, "YOUR CAREER", TEXT);
    }
}
