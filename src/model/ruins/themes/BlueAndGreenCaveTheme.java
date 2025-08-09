package model.ruins.themes;

import view.MyColors;

public class BlueAndGreenCaveTheme extends DualColorDungeonTheme {
    public BlueAndGreenCaveTheme() {
        super(new BlueCaveTheme(),
                new CaveDungeonTheme(MyColors.DARK_GREEN, MyColors.BLACK, MyColors.DARK_GRAY, MyColors.BLACK, MyColors.GRAY));
    }
}
