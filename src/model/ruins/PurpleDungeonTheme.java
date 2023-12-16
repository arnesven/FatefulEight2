package model.ruins;

import view.MyColors;
public class PurpleDungeonTheme extends BrickDungeonTheme {

    private static final MyColors BASE_COLOR = MyColors.BLACK;
    private static final MyColors BRICK_COLOR = MyColors.PURPLE;
    private static final MyColors FLOOR_COLOR = MyColors.DARK_GRAY;
    private static final MyColors DETAIL_COLOR = MyColors.DARK_PURPLE;
    private static final MyColors FLOOR_DETAIL_COLOR = MyColors.DARK_PURPLE;

    public PurpleDungeonTheme() {
        super(BRICK_COLOR, FLOOR_COLOR, DETAIL_COLOR, FLOOR_DETAIL_COLOR);
    }
}
