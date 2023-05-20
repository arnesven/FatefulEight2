package sprites;

import view.MyColors;
import view.sprites.Sprite32x32;

public class DungeonWallSprite extends Sprite32x32 {
    public DungeonWallSprite(int num) {
        super("wall", "quest.png", num, MyColors.GRAY_RED, MyColors.DARK_RED, MyColors.TAN, MyColors.YELLOW);
    }
}
