package view.sprites;

import view.MyColors;

public class DungeonWallSprite extends Sprite32x32 {
    public static DungeonWallSprite DOOR = new DungeonWallSprite(0x31);
    public static DungeonWallSprite UPPER_WALL = new DungeonWallSprite(0x30);
    public static DungeonWallSprite FULL_WALL = new DungeonWallSprite(0x32);
    public static DungeonWallSprite UPPER_T = new DungeonWallSprite(0x33);
    public static DungeonWallSprite VERTI_WALL = new DungeonWallSprite(0x34);

    public DungeonWallSprite(int num) {
        super("wall", "quest.png", num, MyColors.GRAY_RED, MyColors.DARK_RED, MyColors.TAN, MyColors.YELLOW);
    }
}
