package model.states.duel.gauges;

import view.MyColors;
import view.sprites.Sprite;

public class LeftLeaningSegment implements PowerGaugeSegment {

    private static final Sprite[] SPRITES =
            PowerGaugeSegment.makeSprites(8, 24, BACKGROUND_COLOR, FILL_COLOR);
    private static final Sprite[] SPRITES_ANI =
            PowerGaugeSegment.makeSprites(8, 25, MyColors.WHITE, MyColors.YELLOW);
    private final int xShift;

    public LeftLeaningSegment(int xShift) {
        this.xShift = xShift;
    }

    public LeftLeaningSegment() {
        this(0);
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
