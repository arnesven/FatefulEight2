package model.ruins.objects;

import model.ruins.themes.DungeonTheme;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite16x16;

import java.util.Random;

public class CaveDecoration extends RoomDecoration {
    private final Sprite sprite;

    public CaveDecoration(int position, Random random, MyColors stoneColor, MyColors shadeColor) {
        super(position, random);
        if (position == RoomDecoration.LOWER_LEFT || position == RoomDecoration.UPPER_LEFT) {
            this.sprite = new Sprite16x16("rock", "dungeon.png", 0x20C,
                    MyColors.BLACK, stoneColor, shadeColor, MyColors.BEIGE);
        } else {
            this.sprite = new Sprite16x16("rock", "dungeon.png", 0x20D,
                    MyColors.BLACK, stoneColor, shadeColor, MyColors.BEIGE);
        }
    }

    @Override
    protected Sprite getSprite(DungeonTheme theme) {
        return sprite;
    }
}
