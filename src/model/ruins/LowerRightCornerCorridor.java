package model.ruins;

import model.ruins.themes.DungeonTheme;
import view.sprites.Sprite;
import view.subviews.DungeonDrawer;

import java.awt.*;

public class LowerRightCornerCorridor extends DungeonRoom {
    public LowerRightCornerCorridor(DungeonRoom dungeonRoom) {
        super(dungeonRoom);
    }

    @Override
    public void drawYourself(DungeonDrawer drawer, Point position, boolean connectLeft, boolean connectRight, boolean leftCorner, boolean rightCorner, boolean corridorLeft, boolean corridorRight, DungeonTheme theme) {
        int xStart = position.x;
        int yStart = position.y;
        innerDrawRoom(drawer, xStart, yStart, getWidth(), getHeight(),
                connectLeft, connectRight, leftCorner, rightCorner, corridorLeft, corridorRight, theme);
        drawer.put(xStart + 4 * getWidth(), yStart + 4 * getWidth(), theme.getMid()[3]);
        Sprite corner = corridorRight ? theme.getMid()[3] : theme.getRight()[3];
        drawer.put(xStart + 4*(1+getWidth()), yStart + 8, corner);
    }

    @Override
    public int getMapRoomSpriteNumber() {
        return 0x159;
    }

    @Override
    public boolean hasLowerRightCorner() {
        return false;
    }
}
