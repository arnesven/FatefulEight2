package model.combat;

import model.characters.GameCharacter;
import view.MyColors;
import view.sprites.CharSprite;
import view.sprites.Sprite;

public class SummonCondition extends Condition {
    private static final Sprite CONDITION_SPRITE = CharSprite.make((char) (0xD3), MyColors.PURPLE, MyColors.BLACK, MyColors.CYAN);
    private final GameCharacter summon;

    public SummonCondition(GameCharacter summon) {
        super("Summon", "SMN");
        this.summon = summon;
    }

    @Override
    protected boolean noCombatTurn() {
        return false;
    }

    @Override
    public boolean removeAtEndOfCombat() {
        return true;
    }

    @Override
    public Sprite getSymbol() {
        return CONDITION_SPRITE;
    }

    public GameCharacter getSummon() {
        return summon;
    }
}
