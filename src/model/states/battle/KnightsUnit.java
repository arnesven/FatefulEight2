package model.states.battle;

import view.MyColors;
import view.sprites.Sprite;

public class KnightsUnit extends BattleUnit implements MountedBattleUnit {
    private final Sprite[] spritesFew;
    private final Sprite[] sprites;

    public KnightsUnit(int count, String origin, MyColors color) {
        super("Knights", count, 3, 10, 11, origin);
        this.sprites = makeSpriteSet(2, 0, MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.BROWN, color);
        this.spritesFew = makeSpriteSet(2, 4, MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.BROWN, color);
    }

    @Override
    protected boolean hasFirstStrike() {
        return true;
    }

    @Override
    protected Sprite[] getSprites() {
        if (getCount() < 4) {
            return spritesFew;
        }
        return sprites;
    }
}
