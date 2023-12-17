package model.ruins.themes;

import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class RuinsTheme implements DungeonTheme {

    private static final MyColors BASE_COLOR = MyColors.BLACK;
    private static final MyColors DONT_CARE = MyColors.CYAN;
    private final Sprite32x32[] doorArr;

    private Sprite[] connect;
    private Sprite[] left;
    private Sprite[] mid;
    private Sprite[] right;
    private Sprite stairsDown;
    private Sprite stairsUp;
    private Sprite[] cracked;

    public RuinsTheme(MyColors brickColor, MyColors wallColor, MyColors floorColor, MyColors floorDetail) {
        connect = new Sprite[] {
                new Sprite32x32("dungeontwall", "dungeon.png", 0xC3,
                        BASE_COLOR, brickColor, DONT_CARE, wallColor),
                new Sprite32x32("rightvertidungeonwall", "dungeon.png", 0xC5,
                        BASE_COLOR, floorColor, floorColor, DONT_CARE),
                new Sprite32x32("horidungeonwall", "dungeon.png", 0xC7,
                        BASE_COLOR, brickColor, DONT_CARE, wallColor)
        };

        left = new Sprite[] {
                new Sprite32x32("dungeonul", "dungeon.png", 0xC6,
                        BASE_COLOR, brickColor, BASE_COLOR, wallColor),
                new Sprite32x32("leftvertidungeonwall", "dungeon.png", 0xC5,
                        BASE_COLOR, BASE_COLOR, floorColor, wallColor),
                new Sprite32x32("dungeonll", "dungeon.png", 0xD5,
                        BASE_COLOR, brickColor, BASE_COLOR, wallColor)
        };

        right = new Sprite[] {
                new Sprite32x32("dungeonur", "dungeon.png", 0xD6,
                        BASE_COLOR, brickColor, BASE_COLOR, wallColor),
                new Sprite32x32("rightvertidungeonwall", "dungeon.png", 0xC5,
                        BASE_COLOR, floorColor, BASE_COLOR, wallColor),
                new Sprite32x32("dungeonlr", "dungeon.png", 0xD7,
                        BASE_COLOR, brickColor, BASE_COLOR, wallColor)
        };

        final Sprite HORI_WALL = new Sprite32x32("horidungeonwall", "dungeon.png", 0xC7,
                BASE_COLOR, brickColor, DONT_CARE, wallColor);
        mid = new Sprite[] {
                HORI_WALL,
                new Sprite32x32("dungeonfloor", "dungeon.png", 0xC0,
                        floorDetail, DONT_CARE, DONT_CARE, floorColor),
                HORI_WALL
        };

        stairsUp = new Sprite32x32("stairsup", "dungeon.png", 0xD1,
                BASE_COLOR, brickColor, floorColor, wallColor);
        stairsDown = new Sprite32x32("stairsDown", "dungeon.png", 0xC1,
                BASE_COLOR, brickColor, floorColor, wallColor);

        cracked = new Sprite[] {
            new Sprite32x32("crackedhori", "dungeon.png", 0xB4,
                    BASE_COLOR, brickColor, DONT_CARE, wallColor),
            new Sprite32x32("crackedverti", "dungeon.png", 0xB5,
                    BASE_COLOR, MyColors.GRAY, floorColor, DONT_CARE)
        };

        doorArr = new Sprite32x32[]{
                new Sprite32x32("horidoor", "dungeon.png", 0xD2,
                        BASE_COLOR, brickColor, floorColor, wallColor),
                new Sprite32x32("horileverdoor", "dungeon.png", 0x23,
                        BASE_COLOR, MyColors.PINK, MyColors.RED, wallColor),
                new Sprite32x32("horidoorlocked", "dungeon.png", 0xD0,
                        BASE_COLOR, brickColor, MyColors.BROWN, wallColor),
                new Sprite32x32("vertidoor", "dungeon.png", 0xC4,
                        BASE_COLOR, brickColor, floorColor, wallColor),
                new Sprite32x32("vertileverdoor", "dungeon.png", 0x24,
                        BASE_COLOR, brickColor, floorColor, wallColor),
                new Sprite32x32("vertidoorlocked", "dungeon.png", 0xD4,
                        BASE_COLOR, brickColor, floorColor, wallColor),
                new Sprite32x32("dooroverlay", "dungeon.png", 0xD3,
                        BASE_COLOR, brickColor, floorColor, wallColor)
        };
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
        return mid;
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
        int index = isHorizontal ? 0 : 3;
        index += isLeverDoor ? 1 : 0;
        return doorArr[index];
    }

    @Override
    public Sprite getDoorOverlay() {
        return doorArr[6];
    }

    @Override
    public Sprite getCrackedWall(boolean isHorizontal) {
        return cracked[isHorizontal ? 0 : 1];
    }

    @Override
    public Sprite32x32 getLockedDoor(boolean isHorizontal) {
        int index = isHorizontal ? 2 : 5;
        return doorArr[index];
    }

    @Override
    public Sprite getLever(boolean on) {
        return BrickDungeonTheme.getLeverForOn(on);
    }
}
