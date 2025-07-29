package model.map;

import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;

public class PastSeaHex extends SeaHex {
    private static final PastSeaHexSprite SPRITE_UL = new PastSeaHexSprite(0xA0);
    private static final PastSeaHexSprite SPRITE_UR = new PastSeaHexSprite(0xA4);
    private static final PastSeaHexSprite SPRITE_LL = new PastSeaHexSprite(0xB0);
    private static final PastSeaHexSprite SPRITE_LR = new PastSeaHexSprite(0xB4);

    public PastSeaHex(int state) {
        super(state);
    }

    @Override
    protected Sprite getUpperLeftSprite(MyColors color, int roads, int rivers) {
        return SPRITE_UL;
    }

    @Override
    protected Sprite getUpperRightSprite(MyColors color, int roads, int rivers) {
        return SPRITE_UR;
    }

    @Override
    protected Sprite getLowerLeftSprite(MyColors color, int roads, int rivers) {
        return SPRITE_LL;
    }

    @Override
    protected Sprite getLowerRightSprite(MyColors color, int roads, int rivers) {
        return SPRITE_LR;
    }

    private static class PastSeaHexSprite extends LoopingSprite {
        public PastSeaHexSprite(int num) {
            super("sea", "world.png", num,16, 16);
            this.setColor1(MyColors.BLUE);
            this.setColor2(MyColors.LIGHT_BLUE);
            setFrames(4);
            setDelay(16);
        }
    }
}
