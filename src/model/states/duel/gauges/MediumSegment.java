package model.states.duel.gauges;

import view.MyColors;
import view.sprites.Sprite;

public class MediumSegment implements PowerGaugeSegment {

    private static final Sprite[] SPRITES =
            PowerGaugeSegment.makeSprites(0, 0, BACKGROUND_COLOR, FILL_COLOR);
    private static final Sprite[] SPRITES_ANI =
            PowerGaugeSegment.makeSprites(0, 2, MyColors.WHITE, MyColors.YELLOW);
    private final int xShift;

    public MediumSegment(int xShift) {
        this.xShift = xShift;
    }

    public MediumSegment() {
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
