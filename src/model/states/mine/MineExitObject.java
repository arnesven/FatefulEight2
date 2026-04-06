package model.states.mine;

import model.Model;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;

public class MineExitObject extends MineObject {

    private static final Sprite[] SPRITES =  new Sprite[]{
            new Sprite32x32("mineexitnorth", "warehouse.png", 0x03, MyColors.LIGHT_YELLOW, MyColors.LIGHT_GRAY, MyColors.GRAY_RED),
            new Sprite32x32("mineexitwest", "warehouse.png", 0x12, MyColors.LIGHT_YELLOW, MyColors.LIGHT_GRAY, MyColors.GRAY_RED),
            new Sprite32x32("mineexiteast", "warehouse.png", 0x13, MyColors.LIGHT_YELLOW, MyColors.LIGHT_GRAY, MyColors.GRAY_RED),
            new Sprite32x32("mineexitsouth", "warehouse.png", 0x02, MyColors.LIGHT_YELLOW, MyColors.LIGHT_GRAY, MyColors.GRAY_RED)};

    private final Sprite sprite;

    public MineExitObject(int direction) {
        this.sprite = SPRITES[direction];
    }

    @Override
    public void drawYourself(ScreenHandler screenHandler, LogicalMine mine, Point screenPosition) {
        screenHandler.put(screenPosition.x, screenPosition.y, sprite);
    }

    @Override
    public Point gotMovedInto(Model model, AdvancedMineEvent state, Point currentLocation) {
        state.askToExit(model);
        return currentLocation;
    }
}
