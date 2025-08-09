package model.ruins.themes;

import view.MyColors;

public class GreenAndYellowBrickTheme extends DualColorDungeonTheme {
    public GreenAndYellowBrickTheme() {
        super(new GreenBrickTheme(),
                new BrickDungeonTheme(MyColors.GOLD, MyColors.TAN, MyColors.DARK_GRAY, MyColors.DARK_GRAY));
    }
}
