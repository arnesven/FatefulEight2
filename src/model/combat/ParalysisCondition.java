package model.combat;

import view.MyColors;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class ParalysisCondition extends Condition {
    private static final Sprite SPRITE = CharSprite.make((char)(0xC0), MyColors.LIGHT_YELLOW, MyColors.BLACK, MyColors.CYAN);

    public ParalysisCondition() {
        super("Paralysis", "PAR");
    }

    @Override
    protected boolean noCombatTurn() {
        return true;
    }

    @Override
    public Sprite getSymbol() {
        return SPRITE;
    }
}
