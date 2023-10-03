package model.combat;

import view.MyColors;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class InvisibilityCondition extends Condition {

    private static final Sprite SPRITE = CharSprite.make((char)(0xD7), MyColors.BLACK, MyColors.WHITE, MyColors.CYAN);

    public InvisibilityCondition(int duration) {
        super("Invisible", "INV");
        setDuration(duration+1);
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
