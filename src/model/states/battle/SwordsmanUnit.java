package model.states.battle;

import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class SwordsmanUnit extends BattleUnit {
    private final Sprite[] spritesSeven;
    private final Sprite[] spritesFour;
    private final Sprite[] spritesTwo;
    private final MyColors color;

    public SwordsmanUnit(int count, String origin, MyColors color) {
        super("Swordsmen", count, 3, 8, origin);
        this.spritesSeven = makeSpriteSet(color, 0, 0);
        this.spritesFour = makeSpriteSet(color, 0, 4);
        this.spritesTwo = makeSpriteSet(color, 0, 8);
        this.color = color;
    }

    @Override
    public BattleUnit copy() {
        return new SwordsmanUnit(getCount(), getOrigin(), color);
    }

    @Override
    protected Sprite[] getSprites() {
        if (getCount() < 5) {
            return spritesTwo;
        }
        if (getCount() < 9) {
            return spritesFour;
        }
        return spritesSeven;
    }
}
