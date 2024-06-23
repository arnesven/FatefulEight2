package model.states.battle;

import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class SwordsmanUnit extends BattleUnit {
    private final Sprite[] sprites;
    private final MyColors color;

    public SwordsmanUnit(int count, String origin, MyColors color) {
        super("Swordsmen", count, origin);
        this.sprites = makeSpriteSet(color);
        this.color = color;
    }

    private static Sprite[] makeSpriteSet(MyColors color) {
        Sprite[] result = new Sprite[4];
        for (int i = 0; i < result.length; ++i) {
            result[i] = new Sprite32x32("swordsman" + i, "battle.png", i,
                    MyColors.BLACK, MyColors.GRAY, MyColors.LIGHT_GRAY, color);
        }
        return result;
    }

    @Override
    public BattleUnit copy() {
        return new SwordsmanUnit(getCount(), getOrigin(), color);
    }

    @Override
    protected Sprite[] getSprites() {
        return sprites;
    }
}
