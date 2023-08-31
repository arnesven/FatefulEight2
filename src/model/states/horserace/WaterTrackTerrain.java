package model.states.horserace;

import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;
import view.sprites.TrackEffectSprite;
import view.sprites.TrackSprite;

class WaterTrackTerrain extends TrackTerrain {
    private static final Sprite WATER_SPRITE = new TrackSprite(0x30, MyColors.LIGHT_BLUE, MyColors.GREEN, MyColors.CYAN);
    private static final LoopingSprite WATER_EFFECT = new TrackEffectSprite(0x90, MyColors.LIGHT_BLUE, MyColors.CYAN);

    public WaterTrackTerrain() {
        super("Water");
    }

    @Override
    public Sprite getSprite() {
        return WATER_SPRITE;
    }

    @Override
    public Sprite getEffectSprite() {
        return WATER_EFFECT;
    }

    @Override
    public int getMaximumSpeed() {
        return 2;
    }
}
