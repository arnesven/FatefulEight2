package model.states.mine;

import model.Model;
import util.MyRandom;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.MineRockSprite;
import view.sprites.Sprite;
import view.subviews.MineSummaryView;

import java.awt.*;

public abstract class RockMineObject extends MineableObject {

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
    private final boolean isBreakable;

    public RockMineObject(boolean isBreakable) {
        super("Rock", 2);
        this.sprite = SPRITES[(isBreakable ? 4 : 0) + MyRandom.randInt(SPRITES.length/2)];
        this.isBreakable = isBreakable;
    }

    @Override
    public void drawYourself(ScreenHandler screenHandler, LogicalMine mine, Point screenPosition) {
        screenHandler.put(screenPosition.x, screenPosition.y, sprite);
    }

    @Override
    public boolean gotBumpedInto(Model model, AdvancedMineEvent state, Point currentLocation) {
        if (isBreakable) {
            return state.askToMineObject(model, this);
        }
        return false;
    }

    @Override
    public void giveReward(Model model, AdvancedMineEvent advancedMineEvent, MineSummaryView mineSummaryView) {}

    @Override
    public MyColors getAnimationColor() {
        return MyColors.GRAY;
    }
}
