package model.states.mine;

import view.ScreenHandler;
import view.sprites.Sprite;
import java.awt.*;

public class RockDebrisObject extends MineObject {

    private final Sprite sprite;

    public RockDebrisObject(Sprite sprite) {
        this.sprite = sprite;
    }

    @Override
    public void drawYourself(ScreenHandler screenHandler, LogicalMine mine, Point screenPosition) {
        screenHandler.put(screenPosition.x, screenPosition.y, sprite);
    }
}
