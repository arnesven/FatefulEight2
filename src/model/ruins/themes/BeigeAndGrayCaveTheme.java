package model.ruins.themes;

import view.MyColors;

public class BeigeAndGrayCaveTheme extends CaveDualDungeonTheme {
    public BeigeAndGrayCaveTheme() {
        super(new CaveDungeonTheme(MyColors.GRAY_RED, MyColors.DARK_BROWN, MyColors.DARK_GRAY, MyColors.DARK_BROWN, MyColors.BROWN),
                new CaveDungeonTheme(MyColors.GRAY, MyColors.DARK_GRAY, MyColors.DARK_GRAY, MyColors.DARK_BROWN, MyColors.BROWN));
    }
}
