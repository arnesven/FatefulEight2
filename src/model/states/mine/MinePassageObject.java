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
    private final MineDirection direction;

    public MinePassageObject(MineDirection direction) {
        this.direction = direction;
        this.sprite = SPRITES[direction.ordinal()];
    }

    private static Sprite[] makeSprites() {
        Sprite[] result = new Sprite[]{
                new Sprite32x32("passagenorth", "warehouse.png", 0x21, MyColors.GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY_RED),
                new Sprite32x32("passagewest", "warehouse.png", 0x21, MyColors.GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY_RED),
                new Sprite32x32("passageeast", "warehouse.png", 0x21, MyColors.GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY_RED),
                new Sprite32x32("passagesouth", "warehouse.png", 0x21, MyColors.GRAY, MyColors.LIGHT_GRAY, MyColors.GRAY_RED),
                new Sprite32x32("passageup", "warehouse.png", 0x30, MyColors.BROWN, MyColors.DARK_BROWN, MyColors.DARK_GRAY),
                new Sprite32x32("passagedown", "warehouse.png", 0x31, MyColors.BROWN, MyColors.DARK_BROWN, MyColors.DARK_GRAY)};
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
        if (direction == MineDirection.up || direction == MineDirection.down) {
            state.print("Climb " + direction.toString().toLowerCase() + " the ladder? (Y/N) ");
            if (state.yesNoInput()) {
                return state.moveToRoom(model, state, direction);
            }
        }
        return currentPoint;
    }

    @Override
    public Point bumpedWall(Model model, AdvancedMineEvent state, Point avatarPos, Point dxdy) {
        if (direction.getDxDy().equals(dxdy)) {
            return state.moveToRoom(model, state, direction);
        }
        return avatarPos;
    }
}
