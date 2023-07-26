package model.tutorial;

import view.GameView;
import view.help.SubChapterHelpDialog;

public class TutorialCombatAttacks extends SubChapterHelpDialog {
    private static final String TEXT =
            "When you perform an attack with a character, the character makes a " +
            "skill roll (a D10) using the skill associated with that weapon. The modified " +
            "result is used together with the weapon's damage table. The attack does one damage for each number " +
            "in the table which the result is greater than or equal to.\n\n" +
            "E.g. a modified roll of " +
            "8 on the table [5/7/10] would result in 2 damage. A modified roll of 4 on the same " +
            "table would result in 0 damage. A modified roll of 9 on [8/8] would result in 2 damage.\n\n" +
            "A natural roll of 10 is a critical hit and deals double damage.";

    public TutorialCombatAttacks(GameView view) {
        super(view, "Attacks", TEXT);
    }
}
