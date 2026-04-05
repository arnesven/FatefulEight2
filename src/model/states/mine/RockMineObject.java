package model.states.mine;

import model.Model;
import util.MyRandom;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.MineRockSprite;
import view.sprites.Sprite;

import java.awt.*;

public class RockMineObject extends MineObject {

    private static final Sprite[] SPRITES = new Sprite[]{
        new MineRockSprite(0x04, MyColors.BLACK, MyColors.DARK_GRAY),
        new MineRockSprite(0x05, MyColors.BLACK, MyColors.DARK_GRAY),
        new MineRockSprite(0x06, MyColors.BLACK, MyColors.DARK_GRAY),
        new MineRockSprite(0x07, MyColors.BLACK, MyColors.DARK_GRAY),
        new MineRockSprite(0x04, MyColors.DARK_GRAY, MyColors.GRAY),
        new MineRockSprite(0x05, MyColors.DARK_GRAY, MyColors.GRAY),
        new MineRockSprite(0x06, MyColors.DARK_GRAY, MyColors.GRAY),
        new MineRockSprite(0x07, MyColors.DARK_GRAY, MyColors.GRAY)
    };

    private final Sprite sprite;

    public RockMineObject(boolean isBreakable) {
        this.sprite = SPRITES[(isBreakable ? 4 : 0) + MyRandom.randInt(SPRITES.length/2)];
    }

    @Override
    public void drawYourself(ScreenHandler screenHandler, LogicalMine mine, Point screenPosition) {
        screenHandler.put(screenPosition.x, screenPosition.y, sprite);
    }

    @Override
    public boolean gotBumpedInto(Model model, AdvancedMineEvent state, Point currentLocation) {
        return false;
    }
}
