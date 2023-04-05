package view.help;

import view.GameView;

public class TutorialAllies extends HelpDialog {
    private static final String TEXT =
            "Allies are additional combatants that temporarily come " +
            "to the party's aid during combat.\n\n" +
            "Allies are controlled by you and are always considered to be in the front row.";

    public TutorialAllies(GameView view) {
        super(view, "Allies", TEXT);
    }
}
