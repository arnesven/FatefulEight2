package model.combat.conditions;

import model.classes.Skill;
import util.MyLists;
import view.GameView;
import view.MyColors;
import view.help.ConditionHelpDialog;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class FeebleCondition extends Condition {

    private static final Sprite SPRITE = CharSprite.make((char)(0xD9), MyColors.ORC_GREEN, MyColors.BLACK, MyColors.CYAN);

    public FeebleCondition() {
        super("Feeble", "FBL");
    }


    @Override
    protected boolean noCombatTurn() {
        return false;
    }

    @Override
    public int getBonusForSkill(Skill skill) {
        if (Skill.getStrengthSkills().contains(skill)) {
            return -2;
        }
        return 0;
    }

    @Override
    public Sprite getSymbol() {
        return SPRITE;
    }

    @Override
    public ConditionHelpDialog getHelpView(GameView view) {
        return new ConditionHelpDialog(view, this, "A condition indicating that this character " +
                "has become enfeebled, and thus is feeling very week. The character suffers a -2 penalty to " +
                "all Strength-based skills, i.e." +
                MyLists.commaAndJoin(Skill.getStrengthSkills(), Skill::getName) + ".\n\n" +
                "Resting at inns and taverns normally removes this condition.\n\n" +
                "Drinking a Strength potion will also remove the condition.");
    }
}
