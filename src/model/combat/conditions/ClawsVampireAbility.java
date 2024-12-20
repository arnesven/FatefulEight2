package model.combat.conditions;

import model.Model;
import model.actions.PassiveCombatAction;
import model.characters.GameCharacter;
import model.classes.Skill;
import view.GameView;
import view.help.HelpDialog;
import view.help.SubChapterHelpDialog;

public class ClawsVampireAbility extends VampireAbility {
    private static final int BONUS = 3;
    private static final String DESCRIPTION = "Your unarmed melee attacks do 5/7/7/9 damage, " +
            "strike twice, and can score critical hits. You also get a permanent +" +
            BONUS + " bonus to all unarmed attack rolls.";
    private static final int[] DAMAGE_TABLE = new int[]{5, 7, 7, 9};

    public ClawsVampireAbility() {
        super("Claws", 0x94, DESCRIPTION);
    }

    public static boolean canDoAbility(GameCharacter gc) {
        if (gc.hasCondition(VampirismCondition.class)) {
            VampirismCondition cond = (VampirismCondition) gc.getCondition(VampirismCondition.class);
            if (cond.hasClawsAbility()) {
                return true;
            }
        }
        return false;
    }

    public static PassiveCombatAction getPassiveCombatAbility() {
        return new PassiveCombatAction("Claws") {
            @Override
            public boolean canDoAbility(GameCharacter gc) {
                return ClawsVampireAbility.canDoAbility(gc);
            }

            @Override
            public HelpDialog getHelpChapter(Model model) {
                return ClawsVampireAbility.getHelpChapter(model.getView());
            }
        };
    }

    public static HelpDialog getHelpChapter(GameView view) {
        return new SubChapterHelpDialog(view, "Claws",
                "Claws is a vampire ability. With it " + DESCRIPTION.toLowerCase());
    }

    @Override
    public int getBonusForSkill(Skill skill) {
        return skill == Skill.UnarmedCombat ? BONUS : 0;
    }

    @Override
    public HelpDialog makeHelpChapter(GameView view) {
        return getHelpChapter(view);
    }

    public static int[] getDamageTable() {
        return DAMAGE_TABLE;
    }
}
