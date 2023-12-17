package model.ruins.objects;

import model.Model;
import model.ruins.themes.DungeonTheme;
import model.states.ExploreRuinsState;
import view.sprites.Sprite;

import java.awt.*;


public class StairsUp extends DungeonDoor {


    public StairsUp(Point point) {
        super(point.x, point.y);
    }

    @Override
    protected Sprite getSprite(DungeonTheme theme) {
        return theme.getStairsUp();
    }

    @Override
    public String getDescription() {
        return "Stairs going up";
    }

    @Override
    public void doAction(Model model, ExploreRuinsState state) {
        state.ascend();
    }
}
