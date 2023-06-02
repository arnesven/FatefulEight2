package model.combat;

import view.MyColors;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class ErodeCondition extends Condition {
    private static final Sprite CONDITION_SPRITE = CharSprite.make((char) (0xD6), MyColors.RED, MyColors.BLACK, MyColors.CYAN);

    public ErodeCondition() {
        super("Erode", "ERD");
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
