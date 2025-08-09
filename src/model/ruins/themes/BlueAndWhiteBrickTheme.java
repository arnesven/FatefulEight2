package model.ruins.themes;

import view.MyColors;

public class BlueAndWhiteBrickTheme extends DualColorDungeonTheme {
    public BlueAndWhiteBrickTheme() {
        super(new BlueBrickTheme(),
                new BrickDungeonTheme(MyColors.LIGHT_GRAY, MyColors.DARK_GRAY, MyColors.GRAY, MyColors.BLACK));
    }
}
