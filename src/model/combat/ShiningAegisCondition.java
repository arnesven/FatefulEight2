package model.combat;

import view.MyColors;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class ShiningAegisCondition extends Condition {
    private static final Sprite CONDITION_SPRITE = CharSprite.make((char) (0xD1), MyColors.WHITE, MyColors.LIGHT_YELLOW, MyColors.CYAN);

    public ShiningAegisCondition() {
        super("Shining Aegis", "AEG");
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

    public int getArmorBonus() {
        return 3;
    }
}
