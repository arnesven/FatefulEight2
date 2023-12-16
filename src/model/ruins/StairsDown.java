package model.ruins;

import model.Model;
import model.states.ExploreRuinsState;
import view.sprites.Sprite;

import java.awt.Point;

public class StairsDown extends DungeonDoor {

    public StairsDown(Point point) {
        super(point.x, point.y);
    }

    @Override
    protected Sprite getSprite(DungeonTheme theme) {
        return theme.getStairsDown();
    }

    @Override
    public String getDescription() {
        return "Stairs going down";
    }

    @Override
    public void doAction(Model model, ExploreRuinsState state) {
        state.descend();
    }
}
