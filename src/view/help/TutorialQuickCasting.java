package view.help;

import model.actions.QuickCastPassiveCombatAction;
import view.GameView;
import view.help.SubChapterHelpDialog;

public class TutorialQuickCasting extends SubChapterHelpDialog {

    private static final String TEXT = "A character with at least " +
            QuickCastPassiveCombatAction.MINIMUM_RANKS_REQUIRED + " ranks of Spell Casting can perform the " +
            "quick cast ability in combat. Quick Casting allows a character to take a special round at the " +
            "beginning of combat in which a spell may be cast.";

    public TutorialQuickCasting(GameView view) {
        super(view, "Quick Casting", TEXT);
        setLevel(2);
    }
}
