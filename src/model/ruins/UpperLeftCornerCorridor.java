package model.ruins;

import model.ruins.themes.DungeonTheme;
import view.sprites.Sprite;
import view.subviews.DungeonDrawer;

import java.awt.*;

public class UpperLeftCornerCorridor extends DungeonRoom {
    public UpperLeftCornerCorridor(DungeonRoom dungeonRoom) {
        super(dungeonRoom);
    }

    @Override
    public void drawYourself(DungeonDrawer drawer, Point position, boolean connectLeft, boolean connectRight,
                             boolean leftCorner, boolean rightCorner, boolean corridorLeft, boolean corridorRight,
                             DungeonTheme theme) {
        int xStart = position.x;
        int yStart = position.y;
        innerDrawRoom(drawer, xStart, yStart, 1, 1,
                connectLeft, false, leftCorner, false, false, false, theme);
        drawer.put(xStart + 4 * (1+getWidth()), yStart, rightCorner ? theme.getLeft()[2] : DungeonTheme.DARK_SPRITE);

        Sprite corner = corridorLeft ? theme.getMid()[3] : theme.getLeft()[3];
        drawer.put(xStart, yStart + 8, corner);
        drawer.put(xStart + 4, yStart + 8, theme.getMid()[3]);
        drawer.put(xStart + 8, yStart + 8, theme.getRight()[2]);
        drawer.put(xStart + 4*(1+getWidth()), yStart + 8, DungeonTheme.DARK_SPRITE);
    }

    @Override
    public int getMapRoomSpriteNumber() {
        return 0x156;
    }

    @Override
    public boolean hasUpperRightCorner() {
        return false;
    }

    @Override
    public boolean hasLowerLeftCorner() {
        return false;
    }

    @Override
    public boolean hasLowerRightCorner() {
        return true;
    }
}
