package model.states.horserace;

import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.TrackSprite;

class ObstacleTrackTerrain extends TrackTerrain {
    private static final Sprite TREE_SPRITE = new TrackSprite(0x50, MyColors.BROWN, MyColors.GREEN,
            MyColors.DARK_GREEN, MyColors.DARK_GRAY);
    private static final Sprite ROCK_SPRITE = new TrackSprite(0x51, MyColors.DARK_GRAY, MyColors.GREEN,
            MyColors.DARK_GREEN, MyColors.GRAY);
    private final Sprite sprite;

    public ObstacleTrackTerrain() {
        super("Obstacle");
        this.sprite = MyRandom.flipCoin() ? TREE_SPRITE : ROCK_SPRITE;
    }

    @Override
    public Sprite getSprite() {
        return sprite;
    }

    @Override
    public int getMaximumSpeed(HorseRacer racer) {
        return 0;
    }

    @Override
    public boolean canBeEntered() {
        return false;
    }
}
