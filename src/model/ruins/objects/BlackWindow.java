package model.ruins.objects;

import model.ruins.themes.DungeonTheme;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class BlackWindow extends LargeWindow {
    private static final Sprite SPRITE = new Sprite32x32("dungeonwindow", "dungeon.png", 0x57,
            MyColors.BLACK, MyColors.BLACK, MyColors.DARK_GRAY, MyColors.BLACK);
    @Override
    protected Sprite getSprite(DungeonTheme theme) {
        return SPRITE;
    }

    public BlackWindow(int x, int y) {
        super(x, y, false);
    }
}
