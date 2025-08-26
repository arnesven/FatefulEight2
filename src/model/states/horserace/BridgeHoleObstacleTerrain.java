package model.states.horserace;

import view.MyColors;
import view.sprites.Sprite;
import view.sprites.TrackSprite;

public class BridgeHoleObstacleTerrain extends JumpableObstacleTrackTerrain {

    private static final TrackSprite SPRITE = new TrackSprite(0x71, MyColors.DARK_BROWN, MyColors.DARK_BROWN, MyColors.LIGHT_BLUE);

    @Override
    public Sprite getSprite() {
        return SPRITE;
    }
}
