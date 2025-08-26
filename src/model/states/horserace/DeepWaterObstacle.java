package model.states.horserace;

import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class DeepWaterObstacle extends TrackTerrain {
    private static final Sprite WATER = new Sprite32x32("water", "world.png", 0x20, MyColors.LIGHT_BLUE, MyColors.BLUE,
            MyColors.PINK, MyColors.BEIGE);

    public DeepWaterObstacle() {
        super("Deep Water");
    }

    @Override
    public Sprite getSprite() {
        return WATER;
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
