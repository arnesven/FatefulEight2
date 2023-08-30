package model.states;

import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite;

public abstract class TrackTerrain {
    private static final Sprite PATH_SPRITE = new TrackSprite(0x30, MyColors.TAN, MyColors.GREEN);
    private static final Sprite GRASS_SPRITE = new TrackSprite(0x30, MyColors.GREEN, MyColors.GREEN);
    private static final Sprite WATER_SPRITE = new TrackSprite(0x30, MyColors.BLUE, MyColors.GREEN);

    public static TrackTerrain randomTerrain() {
        if (MyRandom.flipCoin()) {
            if (MyRandom.flipCoin()) {
                return new WaterTrackTerrain();
            }
            return new PathTrackTerrain();
        }
        return new GrassTrackTerrain();
    }

    public abstract Sprite getSprite();

    private static class PathTrackTerrain extends TrackTerrain {
        @Override
        public Sprite getSprite() {
            return PATH_SPRITE;
        }
    }

    private static class GrassTrackTerrain extends TrackTerrain {
        @Override
        public Sprite getSprite() {
            return GRASS_SPRITE;
        }
    }

    private static class TrackSprite extends Sprite {
        public TrackSprite(int num, MyColors color1, MyColors color2) {
            super("tracksprite" + num, "riding.png", num % 16, num / 16, 32, 32);
            setColor1(color1);
            setColor2(color2);
        }
    }

    private static class WaterTrackTerrain extends TrackTerrain {
        @Override
        public Sprite getSprite() {
            return WATER_SPRITE;
        }
    }
}
