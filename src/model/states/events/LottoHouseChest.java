package model.states.events;

import model.ruins.objects.DungeonChest;
import model.ruins.themes.DungeonTheme;
import view.subviews.DungeonDrawer;

import java.awt.*;
import java.util.Random;

public class LottoHouseChest extends DungeonChest {

    public LottoHouseChest(Random random) {
        super(random);
    }

    @Override
    public void drawYourself(DungeonDrawer drawer, int xPos, int yPos, DungeonTheme theme) {
        drawer.registerNoRestriction(getSprite(theme).getName(), new Point(xPos, yPos), getSprite(theme));
    }
}
