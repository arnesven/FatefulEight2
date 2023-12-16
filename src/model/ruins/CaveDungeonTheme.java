package model.ruins;

import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class CaveDungeonTheme implements DungeonTheme {

    private static final MyColors BASE_COLOR = MyColors.BLACK;
    private static final MyColors SHADE_COLOR = MyColors.DARK_GRAY;
    private static final MyColors STONE_COLOR = MyColors.GRAY;
    private static final MyColors DONT_CARE = MyColors.CYAN;
    private static final MyColors FLOOR_COLOR = MyColors.GRAY_RED;
    private static final MyColors FLOOR_DETAIL = MyColors.DARK_BROWN;
    private static final MyColors DOOR_COLOR = MyColors.BROWN;

    private static final Sprite LEFT_CORNER = new Sprite32x32("cavedungeonul", "dungeon.png", 0x96,
            BASE_COLOR, STONE_COLOR, SHADE_COLOR, BASE_COLOR);
    private static final Sprite RIGHT_CORNER = new Sprite32x32("cavedungeonur", "dungeon.png", 0xA6,
            BASE_COLOR, STONE_COLOR, SHADE_COLOR, BASE_COLOR);
    private static final Sprite HORI_WALL = new Sprite32x32("cave horidungeonwall", "dungeon.png", 0x97,
            BASE_COLOR, STONE_COLOR, SHADE_COLOR, DONT_CARE);
    private static final Sprite FLOOR = new Sprite32x32("dungeonfloor", "dungeon.png", 0x90,
            FLOOR_DETAIL, DONT_CARE, DONT_CARE, FLOOR_COLOR);
    private static final Sprite STAIRS_DOWN = new Sprite32x32("cavestairsdown", "dungeon.png", 0x91,
            BASE_COLOR, STONE_COLOR, FLOOR_COLOR, SHADE_COLOR);
    private static final Sprite STAIRS_UP = new Sprite32x32("cavestairsup", "dungeon.png", 0xA1,
            BASE_COLOR, STONE_COLOR, FLOOR_COLOR, SHADE_COLOR);
    private static final Sprite32x32 LOCKED_HORI_DOOR = new Sprite32x32("horidoorlocked", "dungeon.png", 0xA0,
            BASE_COLOR, STONE_COLOR, DOOR_COLOR, SHADE_COLOR);
    private static final Sprite32x32 LOCKED_VERTI_DOOR = new Sprite32x32("vertidoorlocked", "dungeon.png", 0xB0,
            FLOOR_COLOR, STONE_COLOR, SHADE_COLOR, DOOR_COLOR);
    private static final Sprite HORI_LEVER_DOOR = new Sprite32x32("horileverdoor", "dungeon.png", 0x23,
            BASE_COLOR, MyColors.PINK, MyColors.RED, DONT_CARE);
    private static final Sprite HORI_DOOR = new Sprite32x32("horidoor", "dungeon.png", 0xA2,
            BASE_COLOR, STONE_COLOR, FLOOR_COLOR, SHADE_COLOR);
    private static final Sprite VERTI_LEVER_DOOR = new Sprite32x32("vertileverdoor", "dungeon.png", 0x24,
            MyColors.RED, MyColors.PINK, MyColors.DARK_RED, DONT_CARE);
    private static final Sprite VERTI_DOOR = new Sprite32x32("vertidoor", "dungeon.png", 0xB2,
            FLOOR_COLOR, STONE_COLOR, SHADE_COLOR, DONT_CARE);
    private static final Sprite DOOR_OVERLAY = new Sprite32x32("dooroverlay", "dungeon.png", 0xB1,
            BASE_COLOR, STONE_COLOR, DONT_CARE, SHADE_COLOR);
    private static final Sprite CRACKED_HORI_WALL = new Sprite32x32("horicrack", "dungeon.png", 0xB6,
            BASE_COLOR, STONE_COLOR, SHADE_COLOR, DONT_CARE);
    private static final Sprite CRACKED_VERTI_WALL = new Sprite32x32("verticrack", "dungeon.png", 0xB7,
            BASE_COLOR, SHADE_COLOR, FLOOR_COLOR, STONE_COLOR);

    private static final Sprite[] CONNECT = new Sprite[]{
            new Sprite32x32("cavetwall", "dungeon.png", 0x93,
                    BASE_COLOR, STONE_COLOR, SHADE_COLOR, DONT_CARE),
            new Sprite32x32("caverightvertidungeonwall", "dungeon.png", 0x94,
                    BASE_COLOR, SHADE_COLOR, FLOOR_COLOR, STONE_COLOR),
            HORI_WALL
    };

    private static final Sprite[] LEFT = new Sprite[]{
            LEFT_CORNER,
            new Sprite32x32("caveleftvertidungeonwall", "dungeon.png", 0x95,
                    BASE_COLOR, SHADE_COLOR, FLOOR_COLOR, STONE_COLOR),
            LEFT_CORNER
    };

    private static final Sprite[] RIGHT = new Sprite[]{
            RIGHT_CORNER,
            new Sprite32x32("caverightvertidungeonwall", "dungeon.png", 0xA5,
                    BASE_COLOR, SHADE_COLOR, FLOOR_COLOR, STONE_COLOR),
            RIGHT_CORNER
    };

    private static final Sprite[] MID = new Sprite[]{HORI_WALL, FLOOR, HORI_WALL};


    @Override
    public Sprite[] getConnect() {
        return CONNECT;
    }

    @Override
    public Sprite[] getLeft() {
        return LEFT;
    }

    @Override
    public Sprite[] getMid() {
        return MID;
    }

    @Override
    public Sprite[] getRight() {
        return RIGHT;
    }

    @Override
    public Sprite getStairsDown() {
        return STAIRS_DOWN;
    }

    @Override
    public Sprite getStairsUp() {
        return STAIRS_UP;
    }

    @Override
    public Sprite getDoor(boolean isHorizontal, boolean isLeverDoor) {
        if (isHorizontal) {
            if (isLeverDoor) {
                return HORI_LEVER_DOOR;
            }
            return HORI_DOOR;
        }
        if (isLeverDoor) {
            return VERTI_LEVER_DOOR;
        }
        return VERTI_DOOR;
    }

    @Override
    public Sprite getDoorOverlay() {
        return DOOR_OVERLAY;
    }

    @Override
    public Sprite getCrackedWall(boolean isHorizontal) {
        if (isHorizontal) {
            return CRACKED_HORI_WALL;
        }
        return CRACKED_VERTI_WALL;
    }

    @Override
    public Sprite32x32 getLockedDoor(boolean isHorizontal) {
        if (isHorizontal) {
            return LOCKED_HORI_DOOR;
        }
        return LOCKED_VERTI_DOOR;
    }

    @Override
    public Sprite getLever(boolean on) {
        return BrickDungeonTheme.getLeverForOn(on);
    }
}
