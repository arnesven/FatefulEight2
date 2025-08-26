package model.states.horserace;

import view.MyColors;
import view.sprites.Sprite;
import view.sprites.TrackSprite;

public class BridgeTrackTerrain extends TrackTerrain {

    private static final Sprite SPRITE = new TrackSprite(0x70, MyColors.GREEN, MyColors.DARK_BROWN);

    public BridgeTrackTerrain() {
        super("Bridge");
    }

    @Override
    public Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getMaximumSpeed(HorseRacer racer) {
        return 7;
    }
}
