package model.combat;

import view.MyColors;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class WeakenCondition extends Condition {
    private static final Sprite SPRITE = CharSprite.make((char) (0xC2), MyColors.LIGHT_RED, MyColors.BLACK, MyColors.CYAN);

    public WeakenCondition() {
        super("Weakened", "WKN");
        setDuration(4);

    }

    @Override
    protected boolean noCombatTurn() {
        return false;
    }

    @Override
    public Sprite getSymbol() {
        return SPRITE;
    }
}