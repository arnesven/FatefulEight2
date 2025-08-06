package model.ruins.themes;

import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class DarknessDungeonTheme implements DungeonTheme {
    private static final Sprite32x32 BLACK_SPRITE = new Sprite32x32("allblackdungeon", "dungeon.png", 0x00,
            MyColors.BLACK, MyColors.BLACK, MyColors.BLACK, MyColors.BLACK);
    private static final Sprite[] ALL_BLACK = new Sprite[]{BLACK_SPRITE, BLACK_SPRITE, BLACK_SPRITE, BLACK_SPRITE};

    @Override
    public Sprite[] getConnect() {
        return ALL_BLACK;
    }

    @Override
    public Sprite[] getLeft() {
        return ALL_BLACK;
    }

    @Override
    public Sprite[] getMid() {
        return ALL_BLACK;
    }

    @Override
    public Sprite[] getRight() {
        return ALL_BLACK;
    }

    @Override
    public Sprite getStairsDown() {
        return BLACK_SPRITE;
    }

    @Override
    public Sprite getStairsUp() {
        return BLACK_SPRITE;
    }

    @Override
    public Sprite getDoor(boolean isHorizontal, boolean isLeverDoor) {
        return BLACK_SPRITE;
    }

    @Override
    public Sprite getDoorOverlay() {
        return BLACK_SPRITE;
    }

    @Override
    public Sprite getCrackedWall(boolean isHorizontal) {
        return BLACK_SPRITE;
    }

    @Override
    public Sprite32x32 getLockedDoor(boolean isHorizontal) {
        return BLACK_SPRITE;
    }

    @Override
    public Sprite getLever(boolean on) {
        return BLACK_SPRITE;
    }
}
