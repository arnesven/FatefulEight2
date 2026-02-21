package view.help;

import model.combat.abilities.DefendCombatAction;
import view.GameView;

public class TutorialDefending extends SubChapterHelpDialog {
    private static final String TEXT =
            "A character with at least " + DefendCombatAction.DEFEND_SKILL_RANKS +
            " ranks of Axes, Blades, Blunt Weapons, Polearms or Unarmed Combat and equipped with that type of weapon, can " +
            "perform the Defend ability. The character enters a defensive stance and receives an " +
            "innate 20% block chance until the end of their next turn.";

    public TutorialDefending(GameView view) {
        super(view, "Defending", TEXT);
        setLevel(2);
    }
}
