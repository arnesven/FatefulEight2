package model.ruins.themes;

import view.MyColors;

public class PurpleAndBlackTheme extends DualColorDungeonTheme {
    public PurpleAndBlackTheme() {
        super(new RuinsTheme(MyColors.PURPLE, MyColors.DARK_PURPLE, MyColors.DARK_GRAY, MyColors.DARK_PURPLE),
              new RuinsTheme(MyColors.BLACK, MyColors.DARK_PURPLE, MyColors.DARK_GRAY, MyColors.BLACK));
    }
}
