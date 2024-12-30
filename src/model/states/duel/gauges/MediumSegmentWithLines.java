package model.states.duel.gauges;

import view.MyColors;
import view.sprites.Sprite;

public class MediumSegmentWithLines implements PowerGaugeSegment {

    private static final Sprite[] SPRITES_ANI_WITH_LINE =
            PowerGaugeSegment.makeSprites(0, 3, MyColors.WHITE, MyColors.YELLOW);
    private static final Sprite[] SPRITES_WITH_LINE =
            PowerGaugeSegment.makeSprites(0, 1, BACKGROUND_COLOR, FILL_COLOR);
    private final int xShift;

    public MediumSegmentWithLines(int xShift) {
        this.xShift = xShift;
    }

    public MediumSegmentWithLines() {
        this(0);
    }

    @Override
    public int getXShift() {
        return xShift;
    }

    @Override
    public Sprite[] getNormalSpriteSet() {
        return SPRITES_WITH_LINE;
    }

    @Override
    public Sprite[] getAnimatedSpriteSet() {
        return SPRITES_ANI_WITH_LINE;
    }

    @Override
    public int getWidth() {
        return 2;
    }
}
