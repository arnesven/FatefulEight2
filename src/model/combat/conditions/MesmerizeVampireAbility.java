package model.combat.conditions;

import model.Model;
import model.actions.CombatAction;
import model.characters.GameCharacter;
import model.combat.Combatant;
import model.enemies.Enemy;
import model.enemies.HumanoidEnemy;
import model.states.CombatEvent;
import view.GameView;
import view.help.HelpDialog;
import view.help.SubChapterHelpDialog;

public class MesmerizeVampireAbility extends VampireAbility {
    private static final String NAME = "Mesmerize";
    private static final String DESCRIPTION = "Useful while feeding to make awake victims " +
            "fall asleep, or during combat to paralyze humanoid enemies.";

    public MesmerizeVampireAbility() {
        super(NAME, 0xA2, DESCRIPTION);
    }

    public static boolean canDoAbility(GameCharacter performer, Combatant target) {
        if (!(target instanceof Enemy)) {
            return false;
        }
        if (performer.hasCondition(VampirismCondition.class)) {
            VampirismCondition vampCond = (VampirismCondition) performer.getCondition(VampirismCondition.class);
            if (vampCond.hasMesmerizeAbility()) {
                return true;
            }
        }
        return false;
    }

    public static CombatAction makeCombatAbility() {
        return new CombatAction(NAME, true, false) {
            @Override
            public HelpDialog getHelpChapter(Model model) {
                return MesmerizeVampireAbility.getHelpChapter(model.getView());
            }

            @Override
            protected void doAction(Model model, CombatEvent combat, GameCharacter performer, Combatant target) {
                if (target instanceof HumanoidEnemy) {
                    combat.println(performer.getFirstName() + " performs a mesmerizing stare on " + target.getName() + "!");
                    target.addCondition(new TimedParalysisCondition(1));
                    combat.println(target.getName() + " was stunned!");
                } else {
                    combat.println(NAME + " has no effect on " + target.getName() + ".");
                }
            }
        };
    }

    public static HelpDialog getHelpChapter(GameView view) {
        return new SubChapterHelpDialog(view, NAME, NAME + " is a vampire ability which is " + DESCRIPTION.toLowerCase());
    }

    @Override
    public HelpDialog makeHelpChapter(GameView view) {
        return getHelpChapter(view);
    }
}
