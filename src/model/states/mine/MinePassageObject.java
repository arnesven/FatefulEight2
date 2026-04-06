package model.states.mine;

import model.Model;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;

public class MinePassageObject extends MineObject {

    private static final Sprite[] SPRITES =  makeSprites();
    private final Sprite sprite;
    private final int direction;

    public MinePassageObject(int direction) {
        this.direction = direction;
        this.sprite = SPRITES[direction];
    }

    private static Sprite[] makeSprites() {
        Sprite[] result = new Sprite[]{
                new Sprite32x32("passagenorth", "warehouse.png", 0x21, MyColors.GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY_RED),
                new Sprite32x32("passagewest", "warehouse.png", 0x21, MyColors.GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY_RED),
                new Sprite32x32("passageeast", "warehouse.png", 0x21, MyColors.GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY_RED),
                new Sprite32x32("passagesouth", "warehouse.png", 0x21, MyColors.GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY_RED)};
        result[0].setRotation(90);
        result[2].setRotation(180);
        result[3].setRotation(270);
        return result;
    }

    @Override
    public void drawYourself(ScreenHandler screenHandler, LogicalMine mine, Point screenPosition) {
        screenHandler.put(screenPosition.x, screenPosition.y, sprite);
    }

    @Override
    public Point gotMovedInto(Model model, AdvancedMineEvent state, Point currentPoint) {
        return state.moveToRoom(model, state, direction);
    }
}
