package model.states.duel.gauges;

import view.MyColors;
import view.sprites.Sprite;

public class NarrowSegmentWithLines implements PowerGaugeSegment {
    private static final Sprite[] SPRITES_WITH_LINE =
            PowerGaugeSegment.makeSprites(0, 9, BACKGROUND_COLOR, FILL_COLOR);
    private static final Sprite[] SPRITES_ANI_WITH_LINE =
            PowerGaugeSegment.makeSprites(0, 11, MyColors.WHITE, MyColors.YELLOW);

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
        return 1;
    }
}
