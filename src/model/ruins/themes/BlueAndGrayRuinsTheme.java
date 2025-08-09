package model.ruins.themes;

import view.MyColors;

public class BlueAndGrayRuinsTheme extends DualColorDungeonTheme {

    public BlueAndGrayRuinsTheme() {
        super(new BlueRuinsTheme(),
                new RuinsTheme(MyColors.LIGHT_GRAY, MyColors.GRAY, MyColors.DARK_GRAY, MyColors.BLACK));
    }

}
