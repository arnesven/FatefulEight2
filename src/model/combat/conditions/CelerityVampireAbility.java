package model.combat.conditions;

import model.Model;
import model.actions.PassiveCombatAction;
import model.characters.GameCharacter;
import view.GameView;
import view.help.HelpDialog;
import view.help.SubChapterHelpDialog;

public class CelerityVampireAbility extends VampireAbility {
    private static final String DESCRIPTION = "Grants demonic speed in combat, enabling you " +
            "to take an extra combat action at the end of each combat round.";
    private static final String NAME = "Celerity";

    public CelerityVampireAbility() {
        super(NAME, 0xB3, DESCRIPTION);
    }

    public static boolean canDoAbility(GameCharacter gc) {
        if (gc.hasCondition(VampirismCondition.class)) {
            VampirismCondition vampCond = (VampirismCondition) gc.getCondition(VampirismCondition.class);
            if (vampCond.hasCelerityAbility()) {
                return true;
            }
        }
        return false;
    }

    public static PassiveCombatAction getPassiveCombatAbility() {
        return new PassiveCombatAction(NAME) {
            @Override
            public HelpDialog getHelpChapter(Model model) {
                return CelerityVampireAbility.getHelpChapter(model.getView());

            }
        };
    }

    public static HelpDialog getHelpChapter(GameView view) {
        return new SubChapterHelpDialog(view, "Celerity", NAME +
                " is a vampire ability. It " + DESCRIPTION.toLowerCase());
    }
}
