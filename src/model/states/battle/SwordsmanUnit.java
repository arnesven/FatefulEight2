package model.states.battle;

import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class SwordsmanUnit extends BattleUnit {
    private static final Sprite[] SPRITE = makeSpriteSet();

    public SwordsmanUnit(int count) {
        super("Swordsmen", count);
    }

    private static Sprite[] makeSpriteSet() {
        Sprite[] result = new Sprite[4];
        for (int i = 0; i < result.length; ++i) {
            result[i] = new Sprite32x32("swordsman" + i, "battle.png", i,
                    MyColors.BLACK, MyColors.GRAY, MyColors.LIGHT_GRAY, MyColors.LIGHT_RED);
        }
        return result;
    }

    @Override
    protected Sprite[] getSprites() {
        return SPRITE;
    }
}
