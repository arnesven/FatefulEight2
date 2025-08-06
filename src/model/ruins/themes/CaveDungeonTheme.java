package model.ruins.themes;

import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class CaveDungeonTheme implements DungeonTheme {
    private static final MyColors BASE_COLOR = MyColors.BLACK;
    private static final MyColors DONT_CARE = MyColors.CYAN;
    private final Sprite[] connect;
    private final Sprite[] left;
    private final Sprite[] right;
    private final Sprite[] MID;
    private final Sprite stairsDown;
    private final Sprite stairsUp;
    private final Sprite32x32[] doorArray;
    private final Sprite[] crackedWalls;


    public CaveDungeonTheme(MyColors stoneColor, MyColors shadeColor, 
                            MyColors floorColor, MyColors floorDetail, MyColors doorColor) {
        final Sprite LEFT_CORNER = new Sprite32x32("cavedungeonul", "dungeon.png", 0x96,
                BASE_COLOR, stoneColor, shadeColor, BASE_COLOR);
        final Sprite BLACK = new Sprite32x32("cavedungeonul", "dungeon.png", 0x96,
                BASE_COLOR, BASE_COLOR, BASE_COLOR, BASE_COLOR);
        final Sprite RIGHT_CORNER = new Sprite32x32("cavedungeonur", "dungeon.png", 0xA6,
                BASE_COLOR, stoneColor, shadeColor, BASE_COLOR);
        final Sprite HORI_WALL = new Sprite32x32("cave horidungeonwall", "dungeon.png", 0x97,
                BASE_COLOR, stoneColor, shadeColor, DONT_CARE);
        final Sprite FLOOR = new Sprite32x32("dungeonfloor", "dungeon.png", 0x90,
                floorDetail, DONT_CARE, DONT_CARE, floorColor);
        stairsDown = new Sprite32x32("cavestairsdown", "dungeon.png", 0x91,
                BASE_COLOR, stoneColor, floorColor, shadeColor);
        stairsUp = new Sprite32x32("cavestairsup", "dungeon.png", 0xA1,
                BASE_COLOR, stoneColor, floorColor, shadeColor);
        doorArray = new Sprite32x32[]{
            new Sprite32x32("horidoor", "dungeon.png", 0xA2,
                    BASE_COLOR, stoneColor, floorColor, shadeColor),
            new Sprite32x32("vertidoor", "dungeon.png", 0xB2,
                    floorColor, stoneColor, shadeColor, DONT_CARE),
            new Sprite32x32("horileverdoor", "dungeon.png", 0x23,
                    BASE_COLOR, MyColors.PINK, MyColors.RED, DONT_CARE),
            new Sprite32x32("vertileverdoor", "dungeon.png", 0x24,
                    MyColors.RED, MyColors.PINK, MyColors.DARK_RED, DONT_CARE),
            new Sprite32x32("horidoorlocked", "dungeon.png", 0xA0,
                    BASE_COLOR, stoneColor, doorColor, shadeColor),
            new Sprite32x32("vertidoorlocked", "dungeon.png", 0xB0,
                    floorColor, stoneColor, shadeColor, doorColor),
            new Sprite32x32("dooroverlay", "dungeon.png", 0xB1,
                    BASE_COLOR, stoneColor, DONT_CARE, shadeColor),
        };

        crackedWalls = new Sprite[]{
                new Sprite32x32("horicrack", "dungeon.png", 0xB6,
                        BASE_COLOR, stoneColor, shadeColor, DONT_CARE),
                new Sprite32x32("verticrack", "dungeon.png", 0xB7,
                    BASE_COLOR, shadeColor, floorColor, stoneColor)
         };

        connect = new Sprite[]{
                new Sprite32x32("cavetwall", "dungeon.png", 0x93,
                        BASE_COLOR, stoneColor, shadeColor, DONT_CARE),
                new Sprite32x32("caverightvertidungeonwall", "dungeon.png", 0x94,
                        BASE_COLOR, shadeColor, floorColor, stoneColor),
                HORI_WALL
        };

        left = new Sprite[]{
                LEFT_CORNER,
                new Sprite32x32("caveleftvertidungeonwall", "dungeon.png", 0x95,
                        BASE_COLOR, shadeColor, floorColor, stoneColor),
                LEFT_CORNER,
                new Sprite32x32("cavedungeonul", "dungeon.png", 0x96,
                        BASE_COLOR, BASE_COLOR, BASE_COLOR, floorColor)
        };

        right = new Sprite[]{
                RIGHT_CORNER,
                new Sprite32x32("caverightvertidungeonwall", "dungeon.png", 0xA5,
                        BASE_COLOR, shadeColor, floorColor, stoneColor),
                RIGHT_CORNER,
                new Sprite32x32("cavedungeonur", "dungeon.png", 0xA6,
                        BASE_COLOR, BASE_COLOR, BASE_COLOR, floorColor)
        };

        MID = new Sprite[]{HORI_WALL, FLOOR, HORI_WALL, BLACK};
    }


    @Override
    public Sprite[] getConnect() {
        return connect;
    }

    @Override
    public Sprite[] getLeft() {
        return left;
    }

    @Override
    public Sprite[] getMid() {
        return MID;
    }

    @Override
    public Sprite[] getRight() {
        return right;
    }

    @Override
    public Sprite getStairsDown() {
        return stairsDown;
    }

    @Override
    public Sprite getStairsUp() {
        return stairsUp;
    }

    @Override
    public Sprite getDoor(boolean isHorizontal, boolean isLeverDoor) {
        int index = isLeverDoor ? 2 : 0;
        index += isHorizontal ? 0 : 1;
        return doorArray[index];
    }

    @Override
    public Sprite getDoorOverlay() {
        return doorArray[6];
    }

    @Override
    public Sprite getCrackedWall(boolean isHorizontal) {
        return crackedWalls[isHorizontal ? 0 : 1];
    }

    @Override
    public Sprite32x32 getLockedDoor(boolean isHorizontal) {
        int index = isHorizontal ? 4 : 5;
        return doorArray[index];
    }

    @Override
    public Sprite getLever(boolean on) {
        return BrickDungeonTheme.getLeverForOn(on);
    }
}
