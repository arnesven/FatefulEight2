package model.ruins;

import model.Model;
import model.states.ExploreRuinsState;
import view.sprites.Sprite;

import java.awt.*;
import java.io.Serializable;

public abstract class DungeonObject implements Serializable {

    private Point internalPosition;

    public DungeonObject(int x, int y) {
        this.internalPosition = new Point(x, y);
    }

    public Point getInternalPosition() {
        return internalPosition;
    }

    public void drawYourself(Model model, int xPos, int yPos, DungeonTheme theme) {
        model.getScreenHandler().put(xPos, yPos, getSprite(theme));
    }

    protected abstract Sprite getSprite(DungeonTheme theme);

    public abstract String getDescription();

    public void doAction(Model model, ExploreRuinsState state) {
        state.println("Nothing happened...");
    }

    public void entryTrigger(Model model, ExploreRuinsState exploreRuinsState) { }

    protected void setInternalPosition(Point pos) {
        this.internalPosition = pos;
    }
}
