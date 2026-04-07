package model.states.mine;

import view.MyColors;
import view.ScreenHandler;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;

public class MineWallObject extends MineObject {
    private final Sprite32x32 sprite;

    public MineWallObject(int x, int y, Rectangle r) {
        int spriteNum = 0;
        if (x == r.x && y == r.y) {
            spriteNum = 0x22;
        } else if (x == r.x && y == r.y + r.height - 1) {
            spriteNum = 0x32;
        } else if (x == r.x + r.width - 1 && y == r.y) {
            spriteNum = 0x23;
        } else if (x == r.x + r.width - 1 && y == r.y + r.height - 1) {
            spriteNum = 0x33;
        } else if (x == r.x) {
            spriteNum = 0x25;
        } else if (x == r.x + r.width - 1) {
            spriteNum = 0x35;
        } else if (y == r.y) {
            spriteNum = 0x24;
        } else if (y == r.y + r.height - 1){
            spriteNum = 0x34;
        } else {
            spriteNum = 0x10;
        }
        this.sprite = new Sprite32x32("minewall", "warehouse.png", // TODO: Static initialization
                spriteNum, MyColors.DARK_GRAY, MyColors.DARK_GRAY, MyColors.GRAY_RED);
    }

    @Override
    public void drawYourself(ScreenHandler screenHandler, LogicalMine mine, Point screenPosition) {
        screenHandler.put(screenPosition.x, screenPosition.y, sprite);
    }
}
