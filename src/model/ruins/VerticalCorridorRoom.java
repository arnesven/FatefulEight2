package model.ruins;

import model.ruins.themes.DungeonTheme;
import view.subviews.DungeonDrawer;

import java.awt.*;

public class VerticalCorridorRoom extends DungeonRoom {
    public VerticalCorridorRoom(DungeonRoom original) {
        super(original);
    }

    @Override
    public void drawYourself(DungeonDrawer drawer, Point position, boolean connectLeft, boolean connectRight, boolean leftCorner, boolean rightCorner, boolean corridorLeft, boolean corridorRight, DungeonTheme theme) {
        int xStart = position.x;
        int yStart = position.y;
        innerDrawRoom(drawer, xStart, yStart, 1, getHeight(),
                connectLeft, false, leftCorner, false, corridorLeft, false, theme);
        drawer.put(xStart + 4 * (1+getWidth()), yStart, rightCorner ? theme.getLeft()[2] : DungeonTheme.DARK_SPRITE);
    }

    @Override
    public int getMapRoomSpriteNumber() {
        if (isLowerLeftCornerCorridor()) {
            return 0x158;
        }
        return 0x146; // Vertical corridor
    }

    @Override
    public boolean hasLowerRightCorner() {
        return true;
    }

    @Override
    public boolean hasUpperRightCorner() {
        return false;
    }
}
