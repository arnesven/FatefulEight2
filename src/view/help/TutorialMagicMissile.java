package view.help;

import model.combat.abilities.MagicMissileCombatAction;
import view.GameView;

public class TutorialMagicMissile extends SubChapterHelpDialog {
    private static final String TEXT = "A character with at least " + MagicMissileCombatAction.REQUIRED_RANKS + " ranks " +
            "of " + MagicMissileCombatAction.SKILL_TO_USE.getName() + " and equipped with a staff or a wand can " +
            "perform the Magic Missile ability targeting an enemy in combat.\n\n" +
            "The performer tests " + MagicMissileCombatAction.SKILL_TO_USE.getName() + ". If the result is " +
            MagicMissileCombatAction.DIFFICULTY + " or more, the ability will deal damage to the enemy based on the " +
            "result of the skill test.";

    public TutorialMagicMissile(GameView view) {
        super(view, "Magic Missile", TEXT);
        setLevel(2);
    }
}
