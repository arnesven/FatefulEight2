package model.map;

import model.Model;
import util.MyLists;
import util.MyPair;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DiscoveredRoute implements Serializable {

    public static final int SHIP = 0;
    public static final int CARRIAGE = 1;
    public static final int TELEPORT = 2;
    public static final MyColors SHIP_COLOR = MyColors.BLUE;
    public static final MyColors CARRIAGE_COLOR = MyColors.RED;
    public static final MyColors TELEPORT_COLOR = MyColors.PURPLE;
    private static final List<String> DIRECTION_SHORTS = List.of("SE", "S", "SW", "NW", "N", "NE");
    private static final Sprite[] CARRIAGE_SPRITES = makeDirectionSprites(CARRIAGE_COLOR);
    private static final Sprite[] SHIP_SPRITES = makeDirectionSprites(SHIP_COLOR);
    private static final Sprite[] TELE_SPRITES = makeDirectionSprites(TELEPORT_COLOR);

    private final String fromPlace;
    private final String toPlace;
    private final int type;
    private final List<MyPair<Point, Sprite>> mapObjects;

    public DiscoveredRoute(Model model, HexLocation fromLocation, HexLocation toLocation, int type) {
        this.fromPlace = fromLocation.getName();
        this.toPlace = toLocation.getName();
        this.type = type;
        mapObjects = makeMapObjects(model, fromLocation, toLocation);
    }

    public static void uniqueAdd(Model model, List<DiscoveredRoute> discoveredRoutes, HexLocation from, HexLocation to, int typeStr) {
        if (!MyLists.any(discoveredRoutes,
                discoveredRoute -> discoveredRoute.isSame(from.getName(), to.getName(), typeStr) ||
                        discoveredRoute.isSame(to.getName(), from.getName(), typeStr))) {
            discoveredRoutes.add(new DiscoveredRoute(model, from, to, typeStr));
        }
    }

    public boolean isSame(String from, String to, int type) {
        return this.fromPlace.equals(from) && this.toPlace.equals(to) && this.type == type;
    }

    public List<MyPair<Point, Sprite>> getMapObjects() {
        return mapObjects;
    }

    private List<MyPair<Point, Sprite>> makeMapObjects(Model model, HexLocation fromLocation, HexLocation toLocation) {
        model.getWorld().dijkstrasByLand(model.getWorld().getPositionForLocation(fromLocation));
        List<Point> path = model.getWorld().shortestPathToPoint(model.getWorld().getPositionForLocation(toLocation));

        List<MyPair<Point, Sprite>> result = new ArrayList<>();
        Point previous = null;
        for (int i = 0; i < path.size(); ++i) {
            Point position = path.get(i);
            if (previous != null) {
                Sprite dirSprite = getSpriteForDxDy(position, new Point(previous.x - position.x, previous.y - position.y));
                result.add(new MyPair<>(position, dirSprite));
            }
            if (i < path.size() - 1) {
                Point next = path.get(i+1);
                Sprite dirSprite = getSpriteForDxDy(position, new Point(next.x - position.x, next.y - position.y));
                result.add(new MyPair<>(position, dirSprite));
            }
            previous = position;
        }
        return result;
    }

    private Sprite getSpriteForDxDy(Point position, Point dxdy) {
        int dir = Direction.getDirectionForDxDy(position, dxdy);
        String shor = Direction.nameForDirection(dir);
        if (type == SHIP) {
            return SHIP_SPRITES[DIRECTION_SHORTS.indexOf(shor)];
        }
        if (type == TELEPORT) {
            return TELE_SPRITES[DIRECTION_SHORTS.indexOf(shor)];
        }
        return CARRIAGE_SPRITES[DIRECTION_SHORTS.indexOf(shor)];
    }


    private static Sprite[] makeDirectionSprites(MyColors color) {
        Sprite[] sprites = new Sprite32x32[6];
        for (int i = 0; i < sprites.length; ++i) {
            sprites[i] = new Sprite32x32("filterline" + i + color.name(), "world.png", 0x61 + i,
                    color, MyColors.GRAY, MyColors.BEIGE);
        }
        return sprites;
    }
}
