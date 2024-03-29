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

    public int getResistance(HorseRacer racer) {
        return 10 - getMaximumSpeed(racer);
    }

    public static TrackTerrain makeTerrain(char c) {
        switch (c) {
            case 'P':
                return new PathTrackTerrain();
            case 'G':
                return new GrassTrackTerrain();
            case 'W':
                return new WaterTrackTerrain();
            case 'J':
                return new JumpableObstacleTrackTerrain();
            case 'O':
                return new ObstacleTrackTerrain();
        }
        throw new IllegalStateException("Error in building horse race track " + c);
    }
}
