package view.help;

import model.combat.conditions.VampirismCondition;
import view.GameView;

public class VampirismHelpView extends ConditionHelpDialog {
    private static final String TEXT =
            "A condition indicating that this character is turning, or has turned into " +
                    "a vampire. Vampires are commonly feared and shunned by society due to the fact that they prey on innocents for their blood.\n\n" +
            "Initially, this condition is scarcely noticeable by others, but as it progresses " +
            "and the character reaches higher stages of vampirism it will become much more apparent. Specifically other party " +
            "members may not react positively to having a vampire in the party.\n\n" +
            "A character with the Vampirism condition has his or her Health, Stamina, Speed and Carrying Capacity increased in " +
            "proportion to the condition's stage. However, a vampire never recovers stamina by resting in a bed, or drinking potions but must instead " +
            "drink the blood of another living creature to replenish itself.";

    public VampirismHelpView(GameView view, VampirismCondition cond) {
        super(view, cond, makeStageText(cond) + TEXT);
    }

    private static String makeStageText(VampirismCondition cond) {
        return cond.getStageString();
    }
}
