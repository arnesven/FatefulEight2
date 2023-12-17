package model.ruins.themes;

import view.MyColors;

public class GrayCaveTheme extends CaveDungeonTheme {
    private static final MyColors SHADE_COLOR = MyColors.DARK_GRAY;
    private static final MyColors STONE_COLOR = MyColors.GRAY;
    private static final MyColors FLOOR_COLOR = MyColors.GRAY_RED;
    private static final MyColors FLOOR_DETAIL = MyColors.DARK_BROWN;
    private static final MyColors DOOR_COLOR = MyColors.BROWN;

    public GrayCaveTheme() {
        super(STONE_COLOR, SHADE_COLOR, FLOOR_COLOR, FLOOR_DETAIL, DOOR_COLOR);
    }
}
