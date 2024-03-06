package model.combat.conditions;

import view.GameView;
import view.MyColors;
import view.help.ConditionHelpDialog;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class FatigueCondition extends Condition {

    private static final Sprite SPRITE = CharSprite.make((char)(0xD9), MyColors.YELLOW, MyColors.BLACK, MyColors.CYAN);

    public FatigueCondition() {
        super("Fatigue", "FAT");
    }

    @Override
    protected boolean noCombatTurn() {
        return false;
    }

    @Override
    public Sprite getSymbol() {
        return SPRITE;
    }

    @Override
    public boolean removeAtEndOfCombat() {
        return true;
    }

    @Override
    public ConditionHelpDialog getHelpView(GameView view) {
        return new ConditionHelpDialog(view, this, "A condition indicating that this combatant is tired from " +
                "fighting in too heavy armor. Fatigued combatants will lose Stamina Points, or Health Points if they perform " +
                "extraneous actions in combat.");
    }
}
