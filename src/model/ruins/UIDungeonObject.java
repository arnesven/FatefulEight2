package model.ruins;

import model.ruins.objects.DungeonObject;
import model.ruins.themes.DungeonTheme;
import view.subviews.DungeonDrawer;

public abstract class UIDungeonObject extends DungeonObject {

    public UIDungeonObject() {
        super(0, 0);
    }

    @Override
    public void drawYourself(DungeonDrawer drawer, int xPos, int yPos, DungeonTheme theme) {
        drawer.putNoRestriction(xPos, yPos, getSprite(theme));
    }
}
