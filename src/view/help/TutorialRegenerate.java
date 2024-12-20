package view.help;

import model.combat.abilities.RegenerationCombatAction;
import view.GameView;

public class TutorialRegenerate extends SubChapterHelpDialog {
    private static final String TEXT = "A character with at least " + RegenerationCombatAction.REQUIRED_RANKS +
            " ranks of " + RegenerationCombatAction.SKILL_TO_USE.getName() + ", and equipped with a wand or staff " +
            "can perform the Regenerate combat ability in combat.\n\n" +
            "The performer will test " + RegenerationCombatAction.SKILL_TO_USE.getName() + ". If the result is " +
            " " + RegenerationCombatAction.DIFFICULTY + " or more, the target will begin regenerating health. " +
            "The target regenerates 1 health point per turn, for a number of turn depending on the result of " +
            "the skill test.";

    public TutorialRegenerate(GameView view) {
        super(view, "Regenerate", TEXT);
        setLevel(2);
    }
}
