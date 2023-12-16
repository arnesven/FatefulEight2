package model.ruins;

import model.Model;
import model.states.ExploreRuinsState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

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
