package model.states.mine;

import model.Model;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;

public class MineWallObject extends MineObject {
    private final Sprite32x32 sprite;

    private static final Sprite32x32[][] SPRITES = makeWallSprites();

    private static Sprite32x32[][] makeWallSprites() {
        Sprite32x32[][] sprites = new Sprite32x32[5][2];
        for (int y = 0; y < sprites[0].length; ++y) {
            for (int x = 0; x < sprites.length-1; ++x) {
                sprites[x][y] = new Sprite32x32("minewall"+x+":"+y, "warehouse.png",
                        0x20 + 0x10 * y + x + 2, MyColors.DARK_GRAY, MyColors.DARK_GRAY, MyColors.GRAY_RED);
            }
        }
        sprites[4][0] = new Sprite32x32("mineblank", "warehouse.png",
                0x10, MyColors.DARK_GRAY, MyColors.DARK_GRAY, MyColors.GRAY_RED);
        return sprites;
    }

    public MineWallObject(int x, int y, Rectangle r) {
        Sprite32x32 spriteNum;
        if (x == r.x && y == r.y) {
            spriteNum = SPRITES[0][0];
        } else if (x == r.x && y == r.y + r.height - 1) {
            spriteNum = SPRITES[0][1];
        } else if (x == r.x + r.width - 1 && y == r.y) {
            spriteNum = SPRITES[1][0];
        } else if (x == r.x + r.width - 1 && y == r.y + r.height - 1) {
            spriteNum = SPRITES[1][1];
        } else if (x == r.x) {
            spriteNum = SPRITES[3][0];
        } else if (x == r.x + r.width - 1) {
            spriteNum = SPRITES[3][1];
        } else if (y == r.y) {
            spriteNum = SPRITES[2][0];
        } else if (y == r.y + r.height - 1){
            spriteNum = SPRITES[2][1];
        } else {
            spriteNum = SPRITES[4][0];
        }
        this.sprite = spriteNum;
    }

    @Override
    public boolean gotBumpedInto(Model model, AdvancedMineEvent state, Point currentLocation) {
        return false;
    }

    @Override
    public void drawYourself(ScreenHandler screenHandler, LogicalMine mine, Point screenPosition) {
        screenHandler.put(screenPosition.x, screenPosition.y, sprite);
    }
}
