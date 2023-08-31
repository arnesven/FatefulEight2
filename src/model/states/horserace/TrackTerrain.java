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
                if (MyRandom.flipCoin()) {
                    return new WaterTrackTerrain();
                } else {
                    return new ObstacleTrackTerrain();
                }
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

    public abstract int getMaximumSpeed();

    public boolean canBeEntered() {
        return true;
    }

}
