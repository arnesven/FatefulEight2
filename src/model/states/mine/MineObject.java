package model.states.mine;

import model.Model;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.MineRockSprite;
import view.sprites.Sprite;

import java.awt.*;

public abstract class MineObject {
    protected static Sprite[] makeOreSprites(MyColors baseColor, MyColors highlight, MyColors ore) {
        return new Sprite[]{
                new MineRockSprite(0x04, baseColor, highlight, highlight, ore),
                new MineRockSprite(0x05, baseColor, highlight, highlight, ore),
                new MineRockSprite(0x06, baseColor, highlight, highlight, ore),
                new MineRockSprite(0x07, baseColor, highlight, highlight, ore),
                new MineRockSprite(0x04, baseColor, highlight, ore, highlight),
                new MineRockSprite(0x05, baseColor, highlight, ore, highlight),
                new MineRockSprite(0x06, baseColor, highlight, ore, highlight),
                new MineRockSprite(0x07, baseColor, highlight, ore, highlight),
                new MineRockSprite(0x04, baseColor, highlight, ore, ore),
                new MineRockSprite(0x05, baseColor, highlight, ore, ore),
                new MineRockSprite(0x06, baseColor, highlight, ore, ore),
                new MineRockSprite(0x07, baseColor, highlight, ore, ore)
        };
    }

    public abstract void drawYourself(ScreenHandler screenHandler, LogicalMine mine, Point screenPosition);

    public Point gotMovedInto(Model model, AdvancedMineEvent state, Point currentLocation) {
        return currentLocation;
    }

    public boolean gotBumpedInto(Model model, AdvancedMineEvent state, Point currentLocation) {
        return true;
    }
}
