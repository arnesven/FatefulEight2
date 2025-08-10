package model.ruins.objects;

import model.ruins.themes.DungeonTheme;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.DungeonDrawer;

public class StairsRondel extends PositionableRoomDecoration {
    private static final Sprite SPRITE_LEFT = new Sprite32x32("stairsrondelleft", "dungeon.png", 0x86,
            MyColors.BLACK, MyColors.BEIGE, MyColors.BROWN, MyColors.LIGHT_BLUE);
    private static final Sprite SPRITE_RIGHT = new Sprite32x32("stairsrondelright", "dungeon.png", 0x87,
            MyColors.BLACK, MyColors.BEIGE, MyColors.BROWN, MyColors.LIGHT_BLUE);
    private final Sprite sprite;

    public StairsRondel(int x, int y, boolean left) {
        super(x, y);
        this.sprite = left ? SPRITE_LEFT : SPRITE_RIGHT; 
    }

    @Override
    public void drawYourself(DungeonDrawer drawer, int xPos, int yPos, DungeonTheme theme) {
        drawer.put(xPos-2, yPos, sprite);
    }
}
