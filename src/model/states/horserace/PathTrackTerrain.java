package model.states.horserace;

import view.MyColors;
import view.sprites.Sprite;
import view.sprites.TrackSprite;

class PathTrackTerrain extends TrackTerrain {
    private static final Sprite PATH_SPRITE = new TrackSprite(0x33, MyColors.GREEN, MyColors.TAN, MyColors.GREEN);

    public PathTrackTerrain() {
        super("Path");
    }

    @Override
    public Sprite getSprite() {
        return PATH_SPRITE;
    }

    @Override
    public int getMaximumSpeed() {
        return 8;
    }
}
