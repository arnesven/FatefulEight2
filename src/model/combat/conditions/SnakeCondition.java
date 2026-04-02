package model.combat.conditions;

import model.classes.Skill;
import view.GameView;
import view.help.ConditionHelpDialog;
import view.sprites.Sprite;

public class SnakeCondition extends Condition {
    public SnakeCondition() {
        super("Snake", "SNK");
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
            return -2;
        }
        return super.getBonusForSkill(skill);
    }

    @Override
    public ConditionHelpDialog getHelpView(GameView view) {
        return new ConditionHelpDialog(view, this, "A condition indicating that this character " +
                "is carrying a snake and suffers a -2 penalty to all " +
                "Charisma-based skills.");
    }
}
