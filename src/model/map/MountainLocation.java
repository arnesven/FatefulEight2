package model.map;

import model.map.locations.DecorativeHexLocation;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.HexLocationSprite;
import view.sprites.HexSprite;
import view.sprites.Sprite;
import view.sprites.Sprite16x16;

import java.awt.*;

public class MountainLocation extends DecorativeHexLocation {

    private static final Sprite16x16 WEST_ROAD_OVERLAY = new Sprite16x16("westroadoverlay", "world.png", 0x52,
            HexSprite.ROAD_COLOR, MyColors.TRANSPARENT, MyColors.TRANSPARENT, MyColors.TRANSPARENT);
    private static final Sprite16x16 NORTH_WEST_ROAD_OVERLAY = new Sprite16x16("northwestroadoverlay", "world.png", 0x52,
            MyColors.TRANSPARENT, HexSprite.ROAD_COLOR, MyColors.TRANSPARENT, MyColors.TRANSPARENT);
    private static final Sprite16x16 EAST_ROAD_OVERLAY = new Sprite16x16("eastroadoverlay", "world.png", 0x53,
            HexSprite.ROAD_COLOR, MyColors.TRANSPARENT, MyColors.TRANSPARENT, MyColors.TRANSPARENT);
    private static final Sprite16x16 NORTH_EAST_ROAD_OVERLAY = new Sprite16x16("northeastroadoverlay", "world.png", 0x53,
            MyColors.TRANSPARENT, HexSprite.ROAD_COLOR, MyColors.TRANSPARENT, MyColors.TRANSPARENT);

    public MountainLocation() {
        super("mountainlocation");
    }

    @Override
    protected Sprite getLowerSprite() {
        return HexLocationSprite.make("mountainlocationlower", 0xB0, MyColors.BLACK, MyColors.WHITE, MyColors.GRAY);
    }

    @Override
    protected Sprite getUpperSprite() {
        return HexLocationSprite.make("mountainlocationupper", 0xA0, MyColors.BLACK, MyColors.WHITE, MyColors.GRAY);
    }

    public static void drawRoadOverlay(ScreenHandler screenHandler, int x, int y, WorldHex hex) {
        if (hex.getRoadInDirection(Direction.SOUTH_WEST)) {
            if (hex.getRoadInDirection(Direction.SOUTH | Direction.SOUTH_EAST)) {
                screenHandler.register(WEST_ROAD_OVERLAY.getName(), new Point(x, y+2), WEST_ROAD_OVERLAY);
            }
        }

        if (hex.getRoadInDirection(Direction.NORTH | Direction.NORTH_WEST)) {
            if (hex.getRoadInDirection(Direction.SOUTH | Direction.SOUTH_WEST)) {
                screenHandler.register(WEST_ROAD_OVERLAY.getName(), new Point(x, y+2), WEST_ROAD_OVERLAY);
                screenHandler.register(NORTH_WEST_ROAD_OVERLAY.getName(), new Point(x, y+2), NORTH_WEST_ROAD_OVERLAY);
            }
        }

        if (hex.getRoadInDirection(Direction.SOUTH_EAST)) {
            if (hex.getRoadInDirection(Direction.SOUTH | Direction.SOUTH_WEST)) {
                screenHandler.register(EAST_ROAD_OVERLAY.getName(), new Point(x+2, y+2), EAST_ROAD_OVERLAY);
            }
        }

        if (hex.getRoadInDirection(Direction.NORTH | Direction.NORTH_EAST)) {
            if (hex.getRoadInDirection(Direction.SOUTH | Direction.SOUTH_EAST)) {
                screenHandler.register(EAST_ROAD_OVERLAY.getName(), new Point(x+2, y+2), EAST_ROAD_OVERLAY);
                screenHandler.register(NORTH_EAST_ROAD_OVERLAY.getName(), new Point(x+2, y+2), NORTH_EAST_ROAD_OVERLAY);
            }
        }
    }
}
