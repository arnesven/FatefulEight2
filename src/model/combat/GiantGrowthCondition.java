package model.combat;

import model.items.spells.GiantGrowthSpell;
import view.MyColors;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class GiantGrowthCondition extends Condition {
    private static final Sprite CONDITION_SPRITE = CharSprite.make((char) (0xD4), MyColors.BEIGE, MyColors.BLACK, MyColors.CYAN);

    public GiantGrowthCondition() {
        super("Giant Growth", "GRW");
    }

    @Override
    public boolean removeAtEndOfCombat() {
        return true;
    }

    @Override
    protected boolean noCombatTurn() {
        return false;
    }

    @Override
    public Sprite getSymbol() {
        return CONDITION_SPRITE;
    }

    @Override
    public int getAttackBonus() {
        return 2;
    }
}
