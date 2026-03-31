package model.combat.conditions;

import model.classes.Skill;
import view.GameView;
import view.help.ConditionHelpDialog;
import view.sprites.Sprite;

public class PermanentlyInvisibleCondition extends Condition {
    public PermanentlyInvisibleCondition() {
        super("Perma-Invisible", "PNV");
    }

    @Override
    protected boolean noCombatTurn() {
        return false;
    }

    @Override
    public Sprite getSymbol() {
        return InvisibilityCondition.SPRITE;
    }

    @Override
    public int getBonusForSkill(Skill skill) {
        if (Skill.getCharismaSkills().contains(skill)) {
            return -4;
        }
        return super.getBonusForSkill(skill);
    }

    @Override
    public ConditionHelpDialog getHelpView(GameView view) {
        return new ConditionHelpDialog(view, this, "A condition indicating that this character " +
                "has turned invisible, permanently. While invisible, the characters suffers a -4 penalty to all " +
                "Charisma-based skills.");
    }
}
