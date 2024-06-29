package model.states.battle;

import view.MyColors;
import view.sprites.Sprite;

public class MilitiaUnit extends BattleUnit {
    private final Sprite[] spritesEleven;
    private final Sprite[] spritesSeven;
    private final Sprite[] spritesFour;

    public MilitiaUnit(int count, String origin, MyColors color) {
        super("Militia", count, 0, 5, 6, origin);
        color = fixColor(color);
        this.spritesEleven = makeSpriteSet(4, 0, MyColors.BLACK, BattleUnit.UNIFORM_COLOR, MyColors.PEACH, color);
        this.spritesSeven = makeSpriteSet(4, 4, MyColors.BLACK, BattleUnit.UNIFORM_COLOR, MyColors.PEACH, color);
        this.spritesFour = makeSpriteSet(4, 8, MyColors.BLACK, BattleUnit.UNIFORM_COLOR, MyColors.PEACH, color);
    }

    @Override
    protected boolean hasLowMorale() {
        return true;
    }

    @Override
    protected Sprite[] getSprites() {
        if (getCount() < 7) {
            return spritesFour;
        }
        if (getCount() < 11) {
            return spritesSeven;
        }
        return spritesEleven;
    }

}
