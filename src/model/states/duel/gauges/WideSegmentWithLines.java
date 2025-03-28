package model.states.duel.gauges;

import view.MyColors;
import view.sprites.Sprite;

public class WideSegmentWithLines implements PowerGaugeSegment {
    private static final Sprite[] SPRITES =
            PowerGaugeSegment.makeSprites(8, 13, BACKGROUND_COLOR, FILL_COLOR);
    private static final Sprite[] SPRITES_ANI =
            PowerGaugeSegment.makeSprites(8, 15, MyColors.WHITE, MyColors.YELLOW);

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
        return 3;
    }
}
