package model.states.mine;

import view.MyColors;
import view.ScreenHandler;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;

public class MineTunnelSupportObject extends MineObject {
    private static final Sprite SPRITE = new Sprite32x32("minesupportbeams",
            "warehouse.png", 0x20, MyColors.BROWN, MyColors.DARK_BROWN, MyColors.YELLOW, MyColors.GRAY);

    @Override
    public void drawYourself(ScreenHandler screenHandler, LogicalMine mine, Point screenPosition) {
        screenHandler.register(SPRITE.getName(), screenPosition, SPRITE, 3);
    }
}
