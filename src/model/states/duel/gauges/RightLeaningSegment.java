package model.states.duel.gauges;

import view.MyColors;
import view.sprites.Sprite;

public class RightLeaningSegment implements PowerGaugeSegment {
    private static final Sprite[] SPRITES =
            PowerGaugeSegment.makeSprites(8, 26, BACKGROUND_COLOR, FILL_COLOR);
    private static final Sprite[] SPRITES_ANI =
            PowerGaugeSegment.makeSprites(8, 27, MyColors.WHITE, MyColors.YELLOW);
    private final int xShift;

    public RightLeaningSegment(int xShift) {
        this.xShift = xShift;
    }

    @Override
    public int getXShift() {
        return xShift;
    }

    @Override
    public Sprite[] getNormalSpriteSet() {
        return SPRITES;
    }

    @Override
    public Sprite[] getAnimatedSpriteSet() {
        return SPRITES_ANI;
    }

    @Override
    public int getWidth() {
        return 2;
    }
}
