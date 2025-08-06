package model.ruins.objects;

import model.Model;
import model.ruins.themes.DungeonTheme;
import model.states.ExploreRuinsState;
import view.MyColors;
import view.sprites.LoopingSprite;
import view.sprites.Sprite;
import view.subviews.DungeonDrawer;

import java.awt.*;
import java.util.List;

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

    private static class CampfireSprite extends LoopingSprite {
        public CampfireSprite() {
            super("campfire", "dungeon.png", 0x100, 32, 32);
            setColor1(MyColors.BROWN);
            setColor2(MyColors.WHITE);
            setColor3(MyColors.YELLOW);
            setColor4(MyColors.ORANGE);
            setFrames(4);
        }
    }


    public void doAction(Model model, ExploreRuinsState state) { }
}
