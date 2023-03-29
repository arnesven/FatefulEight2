package model.ruins;

import model.Model;
import model.states.ExploreRuinsState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;

import static model.ruins.DungeonRoom.BRICK_COLOR;
import static model.ruins.DungeonRoom.FLOOR_COLOR;

public class StairsDown extends DungeonDoor {
    private static final Sprite32x32 STAIRS_LEFT = new Sprite32x32("vertidoorlocked", "dungeon.png", 0x01,
            MyColors.BLACK, BRICK_COLOR, FLOOR_COLOR, MyColors.DARK_GRAY);

    public StairsDown(Point point) {
        super(point.x, point.y);
    }

    @Override
    protected Sprite getSprite() {
        return STAIRS_LEFT;
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
