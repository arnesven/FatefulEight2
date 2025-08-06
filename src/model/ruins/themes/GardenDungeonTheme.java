package model.ruins.themes;

import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class GardenDungeonTheme implements DungeonTheme {

    private static final MyColors BASE_COLOR = MyColors.BLACK;
    private static final MyColors DONT_CARE = MyColors.CYAN;
    private static final MyColors WALL_COLOR = MyColors.GRAY_RED;
    private static final MyColors BRICK_COLOR = MyColors.DARK_RED;
    private static final MyColors STAIRS_COLOR = MyColors.DARK_BROWN;
    private final Sprite[] connect;
    private final Sprite[] left;
    private final Sprite[] right;
    private final Sprite[] MID;
    private final Sprite stairsDown;
    private final Sprite stairsUp;
    private final Sprite32x32[] doorArray;
    private final Sprite[] crackedWalls;

    public GardenDungeonTheme() {
        this(MyColors.DARK_GREEN, MyColors.GREEN, MyColors.YELLOW, MyColors.BLACK, MyColors.DARK_BROWN, MyColors.GRAY);
    }
    
    public GardenDungeonTheme(MyColors treeColor, MyColors shadeColor, MyColors highLight,
                            MyColors groundColor, MyColors groundDetail, MyColors gateColor) {
        final Sprite BLACK = new Sprite32x32("gardendungeonul", "dungeon.png", 0xE6,
                BASE_COLOR, BASE_COLOR, BASE_COLOR, BASE_COLOR);
        final Sprite LEFT_CORNER = new Sprite32x32("gardendungeonul", "dungeon.png", 0xE6,
                BASE_COLOR, treeColor, shadeColor, highLight);
        final Sprite RIGHT_CORNER = new Sprite32x32("gardendungeonur", "dungeon.png", 0xF6,
                BASE_COLOR, treeColor, shadeColor, highLight);
        final Sprite HORI_WALL = new Sprite32x32("gardenhoridungeonwall", "dungeon.png", 0xE7,
                BASE_COLOR, treeColor, shadeColor, highLight);
        final Sprite FLOOR = new Sprite32x32("dungeonfloor", "dungeon.png", 0x90,
                groundDetail, DONT_CARE, DONT_CARE, groundColor);
        stairsDown = new Sprite32x32("gardenstairsdown", "dungeon.png", 0xF2,
                BASE_COLOR, STAIRS_COLOR, gateColor, DONT_CARE);
        stairsUp = new Sprite32x32("gardenstairsup", "dungeon.png", 0xF3,
                BASE_COLOR, STAIRS_COLOR, gateColor, DONT_CARE);
        doorArray = new Sprite32x32[]{
                new Sprite32x32("horidoor", "dungeon.png", 0xE2,
                        BASE_COLOR, treeColor, gateColor, shadeColor),
                new Sprite32x32("vertidoor", "dungeon.png", 0xE3,
                        groundColor, treeColor, gateColor, DONT_CARE),
                new Sprite32x32("horileverdoor", "dungeon.png", 0x23,
                        BASE_COLOR, MyColors.PINK, MyColors.RED, DONT_CARE),
                new Sprite32x32("vertileverdoor", "dungeon.png", 0x24,
                        MyColors.RED, MyColors.PINK, MyColors.DARK_RED, DONT_CARE),
                new Sprite32x32("horidoorlocked", "dungeon.png", 0xE0,
                        BASE_COLOR, treeColor, gateColor, shadeColor),
                new Sprite32x32("vertidoorlocked", "dungeon.png", 0xE1,
                        groundColor, treeColor, gateColor, gateColor),
                new Sprite32x32("dooroverlay", "dungeon.png", 0xF1,
                        BASE_COLOR, treeColor, DONT_CARE, shadeColor),
        };

        crackedWalls = new Sprite[]{
                new Sprite32x32("horicrack", "dungeon.png", 0xE4,
                        BASE_COLOR, treeColor, shadeColor, highLight),
                new Sprite32x32("verticrack", "dungeon.png", 0xE5,
                        BASE_COLOR, treeColor, shadeColor, highLight)
        };

        final Sprite32x32 VERTI_WALL = new Sprite32x32("gardenvertiwall", "dungeon.png", 0xF7,
                BASE_COLOR, treeColor, shadeColor, highLight);
        connect = new Sprite[]{
                HORI_WALL,
                VERTI_WALL,
                HORI_WALL
        };

        left = new Sprite[]{
                LEFT_CORNER,
                VERTI_WALL,
                LEFT_CORNER,
                VERTI_WALL
        };

        right = new Sprite[]{
                RIGHT_CORNER,
                VERTI_WALL,
                RIGHT_CORNER,
                VERTI_WALL
        };

        MID = new Sprite[]{HORI_WALL, FLOOR, HORI_WALL, HORI_WALL};
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
