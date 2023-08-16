package model.combat;

import view.MyColors;
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
}
