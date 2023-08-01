package view.help;

import model.actions.DefendCombatAction;
import view.GameView;

public class TutorialDefending extends SubChapterHelpDialog {
    private static final String TEXT =
            "A character with at least " + DefendCombatAction.DEFEND_SKILL_RANKS +
            " ranks of Axes, Blades, Blunt Weapons or Polearms can " +
            "perform the Defend ability. The character enters a defensive stance and receives an " +
            "innate 20% block chance for the remainder of one combat round.";

    public TutorialDefending(GameView view) {
        super(view, "Defending", TEXT);
        setLevel(2);
    }
}
