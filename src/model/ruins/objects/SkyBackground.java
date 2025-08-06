package model.ruins.objects;

import model.Model;
import model.ruins.themes.DungeonTheme;
import util.MyRandom;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.DungeonDrawer;

import java.awt.*;

public class SkyBackground extends PositionableRoomDecoration {
    private static final Sprite[] SPRITES = new Sprite[]{
            new Sprite32x32("skybackground1", "dungeon.png", 0x77,
                    MyColors.LIGHT_BLUE, MyColors.WHITE, MyColors.BROWN, MyColors.LIGHT_BLUE),
            new Sprite32x32("skybackground2", "dungeon.png", 0x76,
                    MyColors.LIGHT_BLUE, MyColors.WHITE, MyColors.BROWN, MyColors.LIGHT_BLUE),
            new Sprite32x32("skybackground3", "dungeon.png", 0x75,
                    MyColors.LIGHT_BLUE, MyColors.WHITE, MyColors.BROWN, MyColors.LIGHT_BLUE),
    };
    private final int xShift;
    private final Sprite sprite;

    public SkyBackground(int x, int y, boolean shiftLeft) {
        super(x, y);
        this.xShift = shiftLeft ? -2 : 2;
        this.sprite = SPRITES[MyRandom.randInt(3)];
    }

    @Override
    public void drawYourself(DungeonDrawer drawer, int xPos, int yPos, DungeonTheme theme) {
        drawer.register(sprite.getName(), new Point(xPos + xShift, yPos), sprite);
    }
}
