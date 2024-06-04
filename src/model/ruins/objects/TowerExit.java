package model.ruins.objects;

import model.Model;

import java.awt.*;

public class TowerExit extends DungeonExit {
    private final OpenDoor inner;

    public TowerExit() {
        setInternalPosition(new Point(1, 0));
        this.inner = new OpenDoor(new Point(0, 0), true, "unused");
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos, model.ruins.themes.DungeonTheme theme) {
        inner.drawYourself(model, xPos, yPos, theme);
    }
}
