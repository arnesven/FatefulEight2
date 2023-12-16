package model.ruins;

import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public abstract class BrickDungeonTheme implements DungeonTheme {

    private static final Sprite ON_SPRITE = new Sprite32x32("onlever", "dungeon.png", 0x43,
            MyColors.BLACK, MyColors.GRAY, MyColors.DARK_BROWN, MyColors.PINK);
    private static final Sprite OFF_SPRITE = new Sprite32x32("offlever", "dungeon.png", 0x44,
            MyColors.BLACK, MyColors.GRAY, MyColors.DARK_BROWN, MyColors.PINK);

    private final Sprite[] CONNECT;
    private final Sprite[] LEFT;
    private final Sprite[] MID;
    private final Sprite[] RIGHT;
    private final Sprite STAIRS_UP;
    private final Sprite STAIRS_DOWN;
    private final Sprite32x32[] doorArray;
    private final Sprite32x32 HORI_CRACK;
    private final Sprite32x32 VERTI_CRACK;

    public BrickDungeonTheme(MyColors brickColor, MyColors floorColor, MyColors detailColor, MyColors floorDetailColor) {
        CONNECT = makeConnectArray(MyColors.BLACK, brickColor, floorColor, detailColor);
        LEFT = makeLeftArray(MyColors.BLACK, brickColor, floorColor, detailColor);
        MID = makeMidArray(MyColors.BLACK, brickColor, floorColor, detailColor, floorDetailColor);
        RIGHT = makeRightArray(MyColors.BLACK, brickColor, floorColor, detailColor);
        STAIRS_UP = makeStairsUp(MyColors.BLACK, brickColor, floorColor, detailColor);
        STAIRS_DOWN = makeStairsDown(MyColors.BLACK, brickColor, floorColor, detailColor);
        doorArray = makeDoorArray(MyColors.BLACK, brickColor, floorColor, detailColor);
        HORI_CRACK = makeHorizontalCracked(MyColors.BLACK, brickColor, floorColor, detailColor);
        VERTI_CRACK = makeVerticalCracked(MyColors.BLACK, floorColor, detailColor);
    }

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
        int index = isHorizontal ? 0 : 3;
        index += isLeverDoor ? 1 : 0;
        return doorArray[index];
    }

    @Override
    public Sprite getDoorOverlay() {
        return doorArray[6];
    }

    @Override
    public Sprite getCrackedWall(boolean isHorizontal) {
        if (isHorizontal) {
            return HORI_CRACK;
        }
        return VERTI_CRACK;
    }

    @Override
    public Sprite32x32 getLockedDoor(boolean isHorizontal) {
        int index = isHorizontal ? 2 : 5;
        return doorArray[index];
    }


    protected static Sprite32x32[] makeLeftArray(MyColors baseColor, MyColors brickColor, MyColors floorColor, MyColors detailColor) {
        Sprite32x32 UL_CORNER = new Sprite32x32("dungeonul", "dungeon.png", 0x06,
                baseColor, brickColor, baseColor, detailColor);
        Sprite32x32 VERTI_WALL_LEFT = new Sprite32x32("leftvertidungeonwall", "dungeon.png", 0x05,
                baseColor, baseColor, floorColor, detailColor);
        Sprite32x32 LL_CORNER = new Sprite32x32("dungeonll", "dungeon.png", 0x15,
                baseColor, brickColor, baseColor, detailColor);
        return new Sprite32x32[] {UL_CORNER, VERTI_WALL_LEFT, LL_CORNER};
    }

    protected static Sprite32x32[] makeConnectArray(MyColors baseColor, MyColors brickColor, MyColors floorColor, MyColors detailColor) {
        Sprite32x32 T_WALL = new Sprite32x32("dungeontwall", "dungeon.png", 0x14,
                baseColor, brickColor, baseColor,detailColor);
        Sprite32x32 VERTI_WALL_BOTH = new Sprite32x32("rightvertidungeonwall", "dungeon.png", 0x05,
                baseColor, floorColor, floorColor,detailColor);
        Sprite32x32 HORI_WALL = new Sprite32x32("horidungeonwall", "dungeon.png", 0x07,
                baseColor, brickColor, baseColor,detailColor);
        return new Sprite32x32[] {T_WALL, VERTI_WALL_BOTH, HORI_WALL};
    }

    protected static Sprite32x32[] makeRightArray(MyColors baseColor, MyColors brickColor, MyColors floorColor, MyColors detailColor) {
        Sprite32x32 UR_CORNER = new Sprite32x32("dungeonur", "dungeon.png", 0x16,
                baseColor, brickColor, baseColor, detailColor);
        Sprite32x32 LR_CORNER = new Sprite32x32("dungeonlr", "dungeon.png", 0x25,
                baseColor, brickColor, baseColor, detailColor);
        Sprite32x32 VERTI_WALL_RIGHT = new Sprite32x32("rightvertidungeonwall", "dungeon.png", 0x05,
                baseColor, floorColor, baseColor, detailColor);
        return new Sprite32x32[] {UR_CORNER, VERTI_WALL_RIGHT, LR_CORNER};
    }

    protected static Sprite32x32[] makeMidArray(MyColors baseColor, MyColors brickColor, MyColors floorColor, MyColors detailColor, MyColors floorDetailColor) {
        Sprite32x32 HORI_WALL = new Sprite32x32("horidungeonwall", "dungeon.png", 0x07,
                baseColor, brickColor, baseColor, detailColor);
        Sprite32x32 FLOOR = new Sprite32x32("dungeonfloor", "dungeon.png", 0x00,
                floorDetailColor, floorDetailColor, baseColor, floorColor);
        return new Sprite32x32[] {HORI_WALL, FLOOR, HORI_WALL};
    }

    protected static Sprite32x32 makeStairsDown(MyColors baseColor, MyColors brickColor, MyColors floorColor, MyColors detailColor) {
        return new Sprite32x32("vertidoorlocked", "dungeon.png", 0x01,
                baseColor, brickColor, floorColor, detailColor);
    }

    public static Sprite32x32 makeStairsUp(MyColors baseColor, MyColors brickColor, MyColors floorColor, MyColors detailColor) {
        return new Sprite32x32("vertidoorlocked", "dungeon.png", 0x11,
                baseColor, brickColor, floorColor, detailColor);
    }

    protected static Sprite32x32[] makeDoorArray(MyColors baseColor, MyColors brickColor, MyColors floorColor, MyColors detailColor) {
        Sprite32x32 HORI_DOOR = new Sprite32x32("horidoor", "dungeon.png", 0x12,
                baseColor, brickColor, floorColor, detailColor);
        Sprite32x32 HORI_LEVER_DOOR = new Sprite32x32("horileverdoor", "dungeon.png", 0x23,
                baseColor, MyColors.PINK, MyColors.RED, detailColor);
        Sprite32x32 VERTI_LEVER_DOOR = new Sprite32x32("vertileverdoor", "dungeon.png", 0x24,
                MyColors.RED, MyColors.PINK, MyColors.DARK_RED, detailColor);
        Sprite32x32 VERTI_DOOR = new Sprite32x32("vertidoor", "dungeon.png", 0x22,
                baseColor, brickColor, floorColor, detailColor);
        Sprite32x32 HORI_DOOR_LOCKED = new Sprite32x32("horidoorlocked", "dungeon.png", 0x10,
                baseColor, brickColor, MyColors.BROWN, detailColor);
        Sprite32x32 VERTI_DOOR_LOCKED = new Sprite32x32("vertidoorlocked", "dungeon.png", 0x20,
                baseColor, brickColor, floorColor, detailColor);
        Sprite32x32 DOOR_OVERLAY = new Sprite32x32("dooroverlay", "dungeon.png", 0x30,
                baseColor, brickColor, floorColor, detailColor);
        return new Sprite32x32[]{HORI_DOOR, HORI_LEVER_DOOR, HORI_DOOR_LOCKED, VERTI_DOOR, VERTI_LEVER_DOOR, VERTI_DOOR_LOCKED, DOOR_OVERLAY};
    }

    protected static Sprite32x32 makeHorizontalCracked(MyColors baseColor, MyColors brickColor, MyColors floorColor, MyColors detailColor) {
        return new Sprite32x32("horicrack", "dungeon.png", 0x26,
                baseColor, brickColor, floorColor, detailColor);
    }

    protected static Sprite32x32 makeVerticalCracked(MyColors baseColor, MyColors floorColor, MyColors detailColor) {
        return new Sprite32x32("verticrack", "dungeon.png", 0x27,
                baseColor, floorColor, floorColor, detailColor);
    }

    @Override
    public Sprite getLever(boolean on) {
        return getLeverForOn(on);
    }

    public static Sprite getLeverForOn(boolean on) {
        if (on) {
            return ON_SPRITE;
        }
        return OFF_SPRITE;
    }
}
