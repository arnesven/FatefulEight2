package model.ruins.objects;

import model.Model;
import model.ruins.themes.DungeonTheme;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class FullWallDecoration extends PositionableRoomDecoration {
    private static final Sprite SPRITE = new Sprite32x32("fullwall", "dungeon.png", 0x74,
            MyColors.LIGHT_BLUE, MyColors.GRAY, MyColors.BROWN, MyColors.DARK_GRAY);

    public FullWallDecoration(int x, int y) {
        super(x, y);
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos, DungeonTheme theme) {
        model.getScreenHandler().put(xPos-2, yPos, SPRITE);
    }
}
