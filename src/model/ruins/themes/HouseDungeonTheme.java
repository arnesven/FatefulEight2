package model.ruins.themes;

import model.ruins.objects.CrackedWall;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class HouseDungeonTheme implements DungeonTheme {

    private static final MyColors DETAIL = MyColors.DARK_GRAY;
    private static final MyColors FLOOR = MyColors.DARK_BROWN;
    private static final MyColors WOOD = MyColors.BROWN;
    private static final MyColors DONT_CARE = MyColors.CYAN;
    private static final MyColors BASE_COLOR = MyColors.BLACK;
    private static final MyColors FLOOR_DETAIL = MyColors.BLACK;
    private static final MyColors DOOR_COLOR = MyColors.GRAY_RED;

    private static final Sprite32x32 HORIZONTAL_WALL =
            new Sprite32x32("hwall", "dungeon.png", 0x120,
                    BASE_COLOR, WOOD, DONT_CARE, DETAIL);
    private static final Sprite UNUSED_SPRITE = new Sprite32x32("unused", "dungeon.png", 0x137,
            DONT_CARE, DONT_CARE, DONT_CARE, DONT_CARE);
    private static final Sprite DARK_SPRITE = new Sprite32x32("dark", "dungeon.png", 0x07,
            MyColors.BLACK, MyColors.BLACK, MyColors.BLACK, MyColors.BLACK);
    private static final Sprite CRACKED_WALL_HORI = new Sprite32x32("cracekdwallh", "dungeon.png",
            0x127, BASE_COLOR, WOOD, DONT_CARE, DETAIL);
    private static final Sprite32x32 CRACKED_WALL_VERTI = new Sprite32x32("crackedwallv", "dungeon.png",
            0x27, BASE_COLOR, FLOOR, DONT_CARE, DETAIL);

    private static final Sprite[] CONNECT_SPRITES = new Sprite[]{
            new Sprite32x32("twall", "dungeon.png", 0x124,
                    BASE_COLOR, WOOD, DONT_CARE, DETAIL),
            new Sprite32x32("vwall", "dungeon.png", 0x05,
                    BASE_COLOR, FLOOR, FLOOR, DETAIL),
            HORIZONTAL_WALL
    };
    private static final Sprite[] LEFT_SPRITES = new Sprite[]{
            new Sprite32x32("ulcorner", "dungeon.png", 0x135,
                    BASE_COLOR, WOOD, MyColors.BLACK, DETAIL),
            new Sprite32x32("vwalll", "dungeon.png", 0x05,
                    BASE_COLOR, MyColors.BLACK, FLOOR, DETAIL),
            new Sprite32x32("llcorner", "dungeon.png", 0x125,
                    BASE_COLOR, WOOD, MyColors.BLACK, DETAIL),
            DARK_SPRITE
    };
    private static final Sprite[] RIGHT_SPRITES = {
            new Sprite32x32("urcorner", "dungeon.png", 0x136,
                    BASE_COLOR, WOOD, MyColors.BLACK, DETAIL),
            new Sprite32x32("vwallr", "dungeon.png", 0x05,
                    BASE_COLOR, FLOOR, MyColors.BLACK, DETAIL),
            new Sprite32x32("lrcorner", "dungeon.png", 0x126,
                    BASE_COLOR, WOOD, MyColors.BLACK, DETAIL),
            DARK_SPRITE
    };
    private static final Sprite[] MID_SPRITES = {
            HORIZONTAL_WALL,
            new Sprite32x32("floor", "dungeon.png", 0x123,
                    FLOOR_DETAIL, DONT_CARE, DONT_CARE, FLOOR),
            UNUSED_SPRITE,
            DARK_SPRITE
    };
    private static final Sprite[] STAIRS = {
            new Sprite32x32("stairsdown", "dungeon.png", 0x121,
                    BASE_COLOR, WOOD, FLOOR, DETAIL),
            new Sprite32x32("stairsup", "dungeon.png", 0x122,
                    BASE_COLOR, WOOD, FLOOR, DETAIL)
    };

    private static final Sprite32x32[] DOORS = {
        new Sprite32x32("horidoor", "dungeon.png", 0x131,
                BASE_COLOR, WOOD, FLOOR, DETAIL),
        new Sprite32x32("horileverdoor", "dungeon.png", 0x130,
                BASE_COLOR, WOOD, MyColors.RED, DETAIL),
        new Sprite32x32("vertidoor", "dungeon.png", 0x133,
                BASE_COLOR, WOOD, FLOOR, DETAIL),
        new Sprite32x32("vertileverdoor", "dungeon.png", 0x134,
                MyColors.RED, WOOD, FLOOR, DETAIL),
        new Sprite32x32("lockedhoridoor", "dungeon.png", 0x130,
                BASE_COLOR, WOOD, DOOR_COLOR, DETAIL),
        new Sprite32x32("vertileverdoor", "dungeon.png", 0x134,
                BASE_COLOR, WOOD, FLOOR, DETAIL),
        new Sprite32x32("dooroverlay", "dungeon.png", 0x132,
                BASE_COLOR, WOOD, DONT_CARE, DETAIL)
    };


    @Override
    public Sprite[] getConnect() {
        return CONNECT_SPRITES;
    }

    @Override
    public Sprite[] getLeft() {
        return LEFT_SPRITES;
    }

    @Override
    public Sprite[] getMid() {
        return MID_SPRITES;
    }

    @Override
    public Sprite[] getRight() {
        return HouseDungeonTheme.RIGHT_SPRITES;
    }

    @Override
    public Sprite getStairsDown() {
        return STAIRS[0];
    }

    @Override
    public Sprite getStairsUp() {
        return STAIRS[1];
    }

    @Override
    public Sprite getDoor(boolean isHorizontal, boolean isLeverDoor) {
        int i = isHorizontal ? 0 : 2 + (isLeverDoor ? 1 : 0);
        return DOORS[i];
    }

    @Override
    public Sprite getDoorOverlay() {
        return DOORS[6];
    }

    @Override
    public Sprite getCrackedWall(boolean isHorizontal) {
        if (isHorizontal) {
            return CRACKED_WALL_HORI;
        }
        return CRACKED_WALL_VERTI;
    }

    @Override
    public Sprite32x32 getLockedDoor(boolean isHorizontal) {
        int i = isHorizontal ? 4 : 5;
        return DOORS[i];
    }

    @Override
    public Sprite getLever(boolean on) {
        return RedBrickTheme.getLeverForOn(on);
    }
}
