package view.help;

import model.actions.FairyHealCombatAction;
import view.GameView;

public class TutorialFairyHeal extends SubChapterHelpDialog {
    private static final String TEXT =
            "A character with at least " + FairyHealCombatAction.REQUIRED_RANKS + " ranks of " +
            FairyHealCombatAction.SKILL_TO_USE.getName() + " and armed with a staff or wand can " +
            "perform the Fairy Heal ability in combat.\n\n" +
            "With Fairy Heal the performer tests " + FairyHealCombatAction.SKILL_TO_USE.getName() + ". " +
            "If the result is 7 or greater, the ability will heal the target 1 Health Point or more " +
            "depending on the result.";

    public TutorialFairyHeal(GameView view) {
        super(view, "Fairy Heal", TEXT);
        setLevel(2);
    }
}
