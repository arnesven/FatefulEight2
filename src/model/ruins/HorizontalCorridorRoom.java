package model.ruins;

import model.ruins.themes.DungeonTheme;
import view.sprites.Sprite;
import view.subviews.DungeonDrawer;

import java.awt.*;

public class HorizontalCorridorRoom extends DungeonRoom {
    public HorizontalCorridorRoom(DungeonRoom dungeonRoom) {
        super(dungeonRoom);
    }

    @Override
    public void drawYourself(DungeonDrawer drawer, Point position, boolean connectLeft, boolean connectRight,
                             boolean leftCorner, boolean rightCorner,
                             boolean corridorLeft, boolean corridorRight, DungeonTheme theme) {
        int xStart = position.x;
        int yStart = position.y;
        innerDrawRoom(drawer, xStart, yStart, getWidth(), 1,
                connectLeft, connectRight, leftCorner, rightCorner, false, false, theme);

        Sprite corner = corridorLeft ? theme.getMid()[3] : (connectLeft ? theme.getLeft()[3] : theme.getLeft()[2]);
        drawer.put(xStart, yStart + 8, corner);
        for (int w = 1; w < getWidth()+1; ++w) {
            drawer.put(xStart + 4 * w, yStart + 8, theme.getMid()[3]);
        }
        corner = corridorRight ? theme.getMid()[3] : theme.getRight()[3];
        drawer.put(xStart + 4*(1+getWidth()), yStart + 8, corner);
    }

    @Override
    public int getMapRoomSpriteNumber() {
        return 0x147;
    }

    @Override
    public boolean hasLowerLeftCorner() {
        return false;
    }

    @Override
    public boolean hasLowerRightCorner() {
        return false;
    }
}
