package model.ruins.themes;

import view.MyColors;

public class BlueAndGreenCaveTheme extends CaveDualDungeonTheme {
    public BlueAndGreenCaveTheme() {
        super(new BlueCaveTheme(),
                new CaveDungeonTheme(MyColors.DARK_GREEN, MyColors.BLACK, MyColors.DARK_GRAY, MyColors.BLACK, MyColors.GRAY));
    }
}
