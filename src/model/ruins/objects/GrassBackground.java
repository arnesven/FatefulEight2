package model.ruins.objects;

import model.Model;
import model.ruins.themes.DungeonTheme;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.DungeonDrawer;

import java.awt.*;

public class GrassBackground extends PositionableRoomDecoration {
    private static final Sprite SPRITE = new Sprite32x32("grassbackground", "dungeon.png", 0x77,
            MyColors.LIGHT_GRAY, MyColors.BEIGE, MyColors.BROWN, MyColors.GREEN);
    private final int shift;

    public GrassBackground(int x, int y, boolean left) {
        super(x, y);
        this.shift = left ? -2 : 2;
    }

    @Override
    public void drawYourself(DungeonDrawer drawer, int xPos, int yPos, DungeonTheme theme) {
        drawer.register(SPRITE.getName(), new Point(xPos + shift, yPos), SPRITE);
    }
}
