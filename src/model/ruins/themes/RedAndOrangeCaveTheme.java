package model.ruins.themes;

import view.MyColors;

public class RedAndOrangeCaveTheme extends CaveDualDungeonTheme {

    public RedAndOrangeCaveTheme() {
        super(new RedCaveTheme(),
                new CaveDungeonTheme(MyColors.GOLD, MyColors.BLACK, MyColors.BROWN, MyColors.BLACK, MyColors.DARK_GRAY));
    }
}
