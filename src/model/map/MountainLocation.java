package model.map;

import model.map.locations.DecorativeHexLocation;
import util.Arithmetics;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.HexLocationSprite;
import view.sprites.HexSprite;
import view.sprites.Sprite;
import view.sprites.Sprite16x16;

import java.awt.*;
import java.util.List;

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

    private static boolean shouldDrawSegment(int dirIndex, WorldHex hex) {

        /*

        NW    N    NE
          \ 2 | 3 /
         1   AAA   4
          / 0 | 5 \
        SW    S    SE

        When to draw roads at 0, 1, 4 and 5 respectively?

        0: NE to SW (if clockwise preferred => not NW and not N)
           SE to NW (if clockwise preferred => NOT NE and not N)
           S to N (if clockwise preferred => NOT NE and not SE)

           SE to SW
           S to NW

           S to SW
         y:
         */

        List<Integer> dirs = List.of(Direction.SOUTH, Direction.SOUTH_WEST, Direction.NORTH_WEST,
                Direction.NORTH, Direction.NORTH_EAST, Direction.SOUTH_EAST);

        for (int distance = 1; distance <= 3; ++distance) {
            for (int step = 0; step < distance; ++step) {
                int indexToUse = (dirIndex - step + dirs.size()) % dirs.size();
                int fromDir = dirs.get(indexToUse);
                int toDir = dirs.get((indexToUse + distance) % dirs.size());

                if (hex.getRoadInDirection(fromDir) && hex.getRoadInDirection(toDir)) {
                    if (distance < 3) {
                        return true;
                    }
                    int other1 = dirs.get((indexToUse - 1 + dirs.size()) % dirs.size());
                    int other2 = dirs.get((indexToUse - 2 + dirs.size()) % dirs.size());
                    if (!hex.getRoadInDirection(other1) && !hex.getRoadInDirection(other2)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static void drawRoadOverlay(ScreenHandler screenHandler, int x, int y, WorldHex hex) {
        if (shouldDrawSegment(0, hex)) {
            screenHandler.register(WEST_ROAD_OVERLAY.getName(), new Point(x, y+2), WEST_ROAD_OVERLAY);
        }

        if (shouldDrawSegment(1, hex)) {
            screenHandler.register(NORTH_WEST_ROAD_OVERLAY.getName(), new Point(x, y+2), NORTH_WEST_ROAD_OVERLAY);
        }

        if (shouldDrawSegment(5, hex)) {
            screenHandler.register(EAST_ROAD_OVERLAY.getName(), new Point(x+2, y+2), EAST_ROAD_OVERLAY);
        }

        if (shouldDrawSegment(4, hex)) {
            screenHandler.register(NORTH_EAST_ROAD_OVERLAY.getName(), new Point(x+2, y+2), NORTH_EAST_ROAD_OVERLAY);
        }
    }
}
