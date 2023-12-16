package model.ruins;

import view.MyColors;

public class DefaultDungeonTheme extends BrickDungeonTheme {
    public static final MyColors BRICK_COLOR = MyColors.GRAY;
    public static final MyColors FLOOR_COLOR = MyColors.GRAY_RED;
    private static final MyColors DETAIL_COLOR = MyColors.DARK_GRAY;
    public static final MyColors FLOOR_DETAIL_COLOR = MyColors.DARK_GRAY;

    public DefaultDungeonTheme() {
        super(BRICK_COLOR, FLOOR_COLOR, DETAIL_COLOR, FLOOR_DETAIL_COLOR);
    }
}
