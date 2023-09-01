package model.states.horserace;

import util.MyRandom;
import view.sprites.Sprite;

public abstract class TrackTerrain {
    private String name;

    public TrackTerrain(String name) {
        this.name = name;
    }

    public static TrackTerrain randomTerrain() {
        if (MyRandom.flipCoin()) {
            if (MyRandom.flipCoin()) {
                int roll = MyRandom.rollD10();
                if (roll < 5) {
                    return new WaterTrackTerrain();
                }
                if (roll < 7) {
                    return new JumpableObstacleTrackTerrain();
                }
                return new ObstacleTrackTerrain();
            }
            return new PathTrackTerrain();
        }
        return new GrassTrackTerrain();
    }

    public abstract Sprite getSprite();

    public String getName() {
        return name;
    }

    public Sprite getEffectSprite() {
        return null;
    }

    public abstract int getMaximumSpeed(HorseRacer racer);

    public boolean canBeEntered() {
        return true;
    }

}
