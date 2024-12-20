package view.help;

import model.combat.abilities.CurseCombatAction;
import view.GameView;

public class TutorialCurseAbility extends SubChapterHelpDialog {
    private static final String TEXT = "A character with at least " + CurseCombatAction.REQUIRED_RANKS + " ranks of " +
            CurseCombatAction.SKILL_TO_USE.getName() + " and equipped with a wand or a staff can perform the Curse " +
            "ability targeting an enemy in combat.\n\n" +
            "The performer tests " + CurseCombatAction.SKILL_TO_USE.getName() + ". If the result is " +
            CurseCombatAction.DIFFICULTY + " or more the target is cursed for a number of round depending on the result " +
            "of the skill test. The curse can be one of:\n\n" +
            "Curse of Weakness: The target does reduced damage.\n\n" +
            "Curse of Pain: The target takes damage each round.\n\n" +
            "Curse of Terror: The target is prevented from attacking.\n\n" +
            "Curse of Transfiguration: The target is transformed into an animal which prevents it from " +
            "attacking and temporarily reduces its health.";

    public TutorialCurseAbility(GameView view) {
        super(view, "Curse", TEXT);
        setLevel(2);
    }
}
