package view.help;

import model.actions.MultiShotCombatAction;
import view.GameView;

public class MultiShotHelpChapter extends HelpDialog {
    private static final String TEXT = "A character with at least " + MultiShotCombatAction.BOW_RANKS_REQUIRED + " ranks of the Bow skill can perform " +
            "the " + MultiShotCombatAction.MULTI_SHOT + " ability in combat while a bow is equipped.\n\nWhen performing the ability, the character exhausts 1 Stamina, then performs " +
            "one attack roll on the targeted enemy, and applies the same attack roll to up to four additional different targets.";

    public MultiShotHelpChapter(GameView previous) {
        super(previous, MultiShotCombatAction.MULTI_SHOT, TEXT);
    }
}
