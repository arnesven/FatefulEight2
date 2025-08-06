package model.ruins.objects;

import view.subviews.DungeonDrawer;

import java.awt.*;

public class TowerExit extends DungeonExit {
    private final OpenDoor inner;

    public TowerExit() {
        setInternalPosition(new Point(1, 0));
        this.inner = new OpenDoor(new Point(0, 0), true, "unused");
    }

    @Override
    public void drawYourself(DungeonDrawer drawer, int xPos, int yPos, model.ruins.themes.DungeonTheme theme) {
        inner.drawYourself(drawer, xPos, yPos, theme);
    }
}
