package model.states.mine;

import util.MyRandom;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.MineRockSprite;
import view.sprites.Sprite;

import java.awt.*;

public class RubyRockObject extends MineObject {
   private static final Sprite[] SPRITES = new Sprite[]{
                new MineRockSprite(0x14, MyColors.DARK_GRAY, MyColors.GRAY, MyColors.RED, MyColors.DARK_RED),
                new MineRockSprite(0x15, MyColors.DARK_GRAY, MyColors.GRAY, MyColors.RED, MyColors.DARK_RED),
                new MineRockSprite(0x16, MyColors.DARK_GRAY, MyColors.GRAY, MyColors.RED, MyColors.DARK_RED),
                new MineRockSprite(0x17, MyColors.DARK_GRAY, MyColors.GRAY, MyColors.RED, MyColors.DARK_RED)};
    private final Sprite sprite;

    public RubyRockObject() {
       this.sprite = SPRITES[MyRandom.randInt(4)];
   }

    @Override
    public void drawYourself(ScreenHandler screenHandler, LogicalMine mine, Point screenPosition) {
        screenHandler.put(screenPosition.x, screenPosition.y, sprite);
    }
}
