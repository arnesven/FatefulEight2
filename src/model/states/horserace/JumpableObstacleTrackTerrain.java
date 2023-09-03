package model.states.horserace;

import view.MyColors;
import view.sprites.Sprite;
import view.sprites.TrackSprite;

public class JumpableObstacleTrackTerrain extends TrackTerrain {

    private static final TrackSprite SPRITE = new TrackSprite(0x63, MyColors.BROWN, MyColors.GREEN, MyColors.BLACK);

    public JumpableObstacleTrackTerrain() {
        super("Jumpable Obstacle");
    }

    @Override
    public Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getMaximumSpeed(HorseRacer racer) {
        if (racer.getYShift() > 20) {
            return 0;
        }
        return 8;
    }

    @Override
    public int getResistance(HorseRacer racer) {
        return 5;
    }
}
