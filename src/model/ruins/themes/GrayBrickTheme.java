package model.ruins.themes;

import model.ruins.themes.BrickDungeonTheme;
import view.MyColors;

public class GrayBrickTheme extends BrickDungeonTheme {
    public static final MyColors BRICK_COLOR = MyColors.GRAY;
    public static final MyColors FLOOR_COLOR = MyColors.GRAY_RED;
    private static final MyColors DETAIL_COLOR = MyColors.DARK_GRAY;
    public static final MyColors FLOOR_DETAIL_COLOR = MyColors.DARK_GRAY;

    public GrayBrickTheme() {
        super(BRICK_COLOR, FLOOR_COLOR, DETAIL_COLOR, FLOOR_DETAIL_COLOR);
    }
}
