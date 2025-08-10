package model.ruins.themes;

import view.MyColors;
import view.sprites.Sprite;

public class RedRuinsTheme extends RuinsTheme {
    public RedRuinsTheme() {
        super(MyColors.DARK_RED, MyColors.DARK_GRAY, MyColors.DARK_BROWN, MyColors.GRAY_RED);
    }

    @Override
    public Sprite getLever(boolean on) {
        return RedBrickTheme.getLeverSpriteFor(on);
    }
}
