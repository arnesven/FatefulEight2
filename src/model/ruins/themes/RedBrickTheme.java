package model.ruins.themes;

import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class RedBrickTheme extends BrickDungeonTheme {

    private static final Sprite ON_SPRITE = new Sprite32x32("onlever", "dungeon.png", 0x43,
            MyColors.BLACK, MyColors.GRAY, MyColors.BROWN, MyColors.PINK);
    private static final Sprite OFF_SPRITE = new Sprite32x32("offlever", "dungeon.png", 0x44,
            MyColors.BLACK, MyColors.GRAY, MyColors.BROWN, MyColors.PINK);

    public RedBrickTheme() {
        super(MyColors.DARK_RED, MyColors.DARK_BROWN, MyColors.DARK_GRAY, MyColors.GRAY_RED);
    }

    @Override
    public Sprite getLever(boolean on) {
        if (on) {
            return ON_SPRITE;
        }
        return OFF_SPRITE;
    }
}
