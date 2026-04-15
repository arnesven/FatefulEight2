package model.ruins.objects;

import model.Model;
import model.ruins.themes.DungeonTheme;
import model.states.ExploreRuinsState;
import view.sprites.CampfireSprite;
import view.sprites.Sprite;
import view.subviews.DungeonDrawer;

import java.awt.*;

public class CampfireDungeonObject extends CenterDungeonObject {

    private static final Sprite SPRITE = new CampfireSprite();

    @Override
    protected Sprite getSprite(DungeonTheme theme) {
        return SPRITE;
    }

    @Override
    public void drawYourself(DungeonDrawer drawer, int xPos, int yPos, DungeonTheme theme) {
        drawer.register(getSprite(theme).getName(), new Point(xPos, yPos), getSprite(theme));
    }

    @Override
    public String getDescription() {
        return "A campfire";
    }


    public void doAction(Model model, ExploreRuinsState state) { }
}
