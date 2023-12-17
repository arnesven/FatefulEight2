package model.ruins.objects;

import model.Model;
import model.ruins.themes.DungeonTheme;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite16x16;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class RoomDecoration extends DungeonObject {
    public static final int LOWER_LEFT = 0;
    public static final int UPPER_LEFT = 1;
    public static final int LOWER_RIGHT = 2;
    public static final int UPPER_RIGHT = 3;
    private static final List<Point> roomPositons = List.of(
            new Point(0, 2), new Point(0, 0), new Point(3, 2), new Point(3, 0));
    private static final List<Point> offsets = List.of(
            new Point(1, 1), new Point(1, 2), new Point(0, 1), new Point(0, 2));
    private final Sprite16x16[] LEFT_SPRITES = new Sprite16x16[]{
            new Sprite16x16("barrelleft", "dungeon.png", 0x73,
                    MyColors.BLACK, MyColors.BROWN, MyColors.LIGHT_GRAY, MyColors.DARK_BROWN),
            new Sprite16x16("crateleft", "dungeon.png", 0x63,
                    MyColors.BLACK, MyColors.BROWN, MyColors.DARK_BROWN, MyColors.LIGHT_GRAY),
            new Sprite16x16("bagsleft", "dungeon.png", 0x62,
                    MyColors.BLACK, MyColors.BEIGE, MyColors.TAN, MyColors.LIGHT_GRAY),
            new Sprite16x16("spiderwebleft", "dungeon.png", 0x72,
                    MyColors.WHITE, MyColors.BEIGE, MyColors.TAN, MyColors.LIGHT_GRAY),
            new Sprite16x16("spikesleft", "dungeon.png", 0x76,
                    MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.TAN, MyColors.LIGHT_GRAY)
    };
    private final Sprite16x16[] RIGHT_SPRITES = new Sprite16x16[]{
            new Sprite16x16("barrelleft", "dungeon.png", 0x74,
                    MyColors.BLACK, MyColors.BROWN, MyColors.LIGHT_GRAY, MyColors.DARK_BROWN),
            new Sprite16x16("crateright", "dungeon.png", 0x64,
                    MyColors.BLACK, MyColors.BROWN, MyColors.DARK_BROWN, MyColors.LIGHT_GRAY),
            new Sprite16x16("bagsleft", "dungeon.png", 0x65,
                    MyColors.BLACK, MyColors.BEIGE, MyColors.TAN, MyColors.LIGHT_GRAY),
            new Sprite16x16("spiderwebright", "dungeon.png", 0x75,
                    MyColors.WHITE, MyColors.BEIGE, MyColors.TAN, MyColors.LIGHT_GRAY),
            new Sprite16x16("spikesright", "dungeon.png", 0x66,
                    MyColors.BLACK, MyColors.LIGHT_GRAY, MyColors.TAN, MyColors.LIGHT_GRAY)
    };

    private final Sprite sprite;
    private final Point offset;

    public RoomDecoration(int location, Random random) {
        super(roomPositons.get(location).x, roomPositons.get(location).y);
        this.offset = offsets.get(location);
        if ( location < 2) {
            sprite = LEFT_SPRITES[random.nextInt(LEFT_SPRITES.length)];
        } else {
            sprite = RIGHT_SPRITES[random.nextInt(RIGHT_SPRITES.length)];
        }
    }

    public RoomDecoration(int location, int index) {
        super(roomPositons.get(location).x, roomPositons.get(location).y);
        this.offset = offsets.get(location);
        if ( location < 2) {
            sprite = LEFT_SPRITES[index];
        } else {
            sprite = RIGHT_SPRITES[index];
        }
    }

    @Override
    protected Sprite getSprite(DungeonTheme theme) {
        return sprite;
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos, DungeonTheme theme) {
        model.getScreenHandler().register(sprite.getName(), new Point(xPos+offset.x*2, yPos+offset.y*2), sprite, 1);
    }

    @Override
    public String getDescription() {
        return "SHOULD NOT BE USED";
    }
}
