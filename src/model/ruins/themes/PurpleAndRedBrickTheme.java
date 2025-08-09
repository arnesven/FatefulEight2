package model.ruins.themes;

import view.MyColors;

public class PurpleAndRedBrickTheme extends DualColorDungeonTheme {
    public PurpleAndRedBrickTheme() {
        super(new BrickDungeonTheme(MyColors.PURPLE, MyColors.DARK_BROWN,
                MyColors.DARK_PURPLE, MyColors.GRAY_RED), new RedBrickTheme());
    }
}
