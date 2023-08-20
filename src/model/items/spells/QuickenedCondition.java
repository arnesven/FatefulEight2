package model.items.spells;

import model.combat.Condition;
import view.MyColors;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class QuickenedCondition extends Condition {
    private static final Sprite CONDITION_SPRITE = CharSprite.make((char)(0xC0), MyColors.CYAN, MyColors.BLACK, MyColors.CYAN);

    public QuickenedCondition() {
        super("Quickened", "QCK");
        setDuration(5);
    }

    @Override
    protected boolean noCombatTurn() {
        return false;
    }

    @Override
    public Sprite getSymbol() {
        return CONDITION_SPRITE;
    }

}
