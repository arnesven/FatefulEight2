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
                connectLeft, false, leftCorner, false, false, false, theme);
        drawer.put(xStart + 4 * (1+getWidth()), yStart, rightCorner ? theme.getLeft()[2] : theme.getMid()[3]);
    }

    @Override
    public boolean hasLowerRightCorner() {
        return false;
    }

    @Override
    public boolean hasUpperRightCorner() {
        return false;
    }
}
