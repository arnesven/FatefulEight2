package model.actions;

import model.combat.Condition;
import view.GameView;
import view.MyColors;
import view.help.ConditionHelpDialog;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class SneakAttackCondition extends Condition {

    private static final Sprite SPRITE = CharSprite.make((char)(0xD7), MyColors.BROWN, MyColors.YELLOW, MyColors.CYAN);

    public SneakAttackCondition() {
        super("Sneak Attack", "SNK");
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
        return new ConditionHelpDialog(view, this,
                "A condition which indicates that the character is preparing to " +
                        "perform the Sneak Attack combat ability.");
    }
}
