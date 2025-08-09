package model.ruins.themes;

import view.MyColors;

public class GreenAndRedDungeonTheme extends DualColorDungeonTheme {
    public GreenAndRedDungeonTheme() {
        super(new RuinsTheme(MyColors.DARK_RED, MyColors.DARK_BROWN, MyColors.DARK_GRAY, MyColors.GRAY_RED),
                new RuinsTheme(MyColors.DARK_GREEN, MyColors.DARK_GRAY, MyColors.DARK_GRAY, MyColors.GOLD));
    }
}
