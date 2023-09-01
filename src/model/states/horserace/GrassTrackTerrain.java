package model.states.horserace;

import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;
import view.sprites.TrackEffectSprite;
import view.sprites.TrackSprite;

class GrassTrackTerrain extends TrackTerrain {
    private static final Sprite GRASS_SPRITE = new TrackSprite(0x32, MyColors.GREEN, MyColors.LIGHT_GREEN);
    private static final LoopingSprite GRASS_EFFECT = new TrackEffectSprite(0x80, MyColors.LIGHT_GREEN, MyColors.BROWN);

    public GrassTrackTerrain() {
        super("Grass");
    }

    @Override
    public Sprite getSprite() {
        return GRASS_SPRITE;
    }

    @Override
    public Sprite getEffectSprite() {
        return GRASS_EFFECT;
    }

    @Override
    public int getMaximumSpeed(HorseRacer racer) {
        return 5;
    }
}
