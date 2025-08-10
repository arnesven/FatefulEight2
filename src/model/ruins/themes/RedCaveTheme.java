package model.ruins.themes;

import view.MyColors;
import view.sprites.Sprite;


public class RedCaveTheme extends CaveDungeonTheme {
    public RedCaveTheme() {
        super(MyColors.DARK_RED, MyColors.BLACK, MyColors.DARK_BROWN, MyColors.BLACK, MyColors.BROWN);
    }

    @Override
    public Sprite getLever(boolean on) {
        return RedBrickTheme.getLeverSpriteFor(on);
    }
}
