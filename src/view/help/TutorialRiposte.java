package view.help;

import model.combat.abilities.RiposteCombatAction;
import view.GameView;

public class TutorialRiposte extends SubChapterHelpDialog {
    private static final String TEXT =
            "A character with at least " + RiposteCombatAction.ACROBATICS_RANKS_REQUIREMENT + " ranks of Acrobatics " +
            "may perform the riposte ability in combat. The character must have at least one Point of Stamina " +
            "and have a bladed weapon or polearm equipped.\n\n" +
            "When selecting the Riposte ability the character exhausts 1 Stamina Point and enters a riposte " +
            "stance for at most 5 turns. This stance grants the character an innate 20% evade chance " +
            "(which stacks with evade chance granted from a character's speed) and will trigger a counter-attack " +
            "if a melee attack is successfully evaded. Each counter-attack performed has a 50% chance of breaking the stance.";

    public TutorialRiposte(GameView view) {
        super(view, "Riposte", TEXT);
        setLevel(2);
    }
}
