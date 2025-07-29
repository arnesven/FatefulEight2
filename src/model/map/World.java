package model.map;

import control.FatefulEight;
import model.Model;
import model.map.objects.MapFilter;
import model.map.objects.MapObject;
import model.map.objects.WaterPathDistancesFilter;
import model.states.dailyaction.town.FlagPoleNode;
import model.tasks.DestinationTask;
import util.MyLists;
import util.MyPair;
import util.MyRandom;
import view.DrawingArea;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.CharSprite;
import view.sprites.Sprite;
import view.sprites.SpriteQuestMarker;
import view.subviews.SubView;

import java.awt.*;
import java.util.*;
import java.io.Serializable;
import java.util.List;
import java.util.function.Predicate;

public class World implements Serializable {

    private final WorldType type;
    private Set<WaterPath> waterWays;
    private final Map<CastleLocation, List<Point>> kingdoms;
    private Map<WorldHex, Integer> landNodes;
    //  x   y
    private final WorldHex[][] hexes;
    private final ViewPointMarker cursor = new HexCursorMarker();
    private static final Sprite DESTINATION_SPRITE = new SpriteQuestMarker();
    private Sprite alternativeAvatar = null;
    private int currentState;
    private Rectangle bounds;

    public World(WorldHex[][] hexes, Rectangle bounds, WorldType type) {
        this.hexes = hexes;
        this.bounds = bounds;
        this.type = type;
        makeWaterWays();
        if (type == WorldType.original) {
            kingdoms = findKingdoms();
        } else {
            kingdoms = new HashMap<>();
            currentState = WorldBuilder.EXPAND_EAST | WorldBuilder.EXPAND_NORTH |
                    WorldBuilder.EXPAND_SOUTH | WorldBuilder.EXPAND_WEST;
        }
    }

    public Point translateToScreen(Point logicPosition, Point viewPoint, int mapXRange, int mapYRange) {
        Interval xVals = calcInterval(viewPoint.x, mapXRange, bounds.x, bounds.x + bounds.width);
        Interval yVals = calcInterval(viewPoint.y, mapYRange, bounds.y, bounds.y + bounds.height);
        int startX = (DrawingArea.WINDOW_COLUMNS - mapXRange*4)/2;

        int x = logicPosition.x;
        int y = logicPosition.y;
        int yOffset = SubView.Y_OFFSET;

        int col = x - xVals.from;
        int row = y - yVals.from;

        int screenX = startX + 4*col;
        int y_extra = 2 * (1 - (x % 2));
        int screenY = yOffset - 2 + 4 * row + y_extra;

        return new Point(screenX, screenY);
    }

    public boolean crossesRiver(Point position, int direction) {
        WorldHex hex = getHex(position);
        return hex.getRiversInDirection(direction);
    }

    public void drawYourself(Model model, Point viewPoint, Point partyPosition,
                             int mapXRange, int mapYRange, int yOffset, Point cursorPos,
                             boolean avatarEnabled) {
        int startX = (DrawingArea.WINDOW_COLUMNS - mapXRange*4)/2;
        drawYourself(model, viewPoint, partyPosition, mapXRange, mapYRange, startX, yOffset,
                cursorPos, avatarEnabled, null);
    }

    public void drawYourself(Model model, Point viewPoint, Point partyPosition,
                             int mapXRange, int mapYRange, int yOffset, Point cursorPos,
                             boolean avatarEnabled, MapFilter filter) {
        int startX = (DrawingArea.WINDOW_COLUMNS - mapXRange*4)/2;
        drawYourself(model, viewPoint, partyPosition, mapXRange, mapYRange, startX, yOffset,
                cursorPos, avatarEnabled, filter);
    }

    public void drawYourself(Model model, Point viewPoint, Point partyPosition,
                             int mapXRange, int mapYRange, int startX, int yOffset, Point cursorPos,
                             boolean avatarEnabled, MapFilter filter) {
        ScreenHandler screenHandler = model.getScreenHandler();
        Interval xVals = calcInterval(viewPoint.x, mapXRange, bounds.x, bounds.x + bounds.width);
        Interval yVals = calcInterval(viewPoint.y, mapYRange, bounds.y, bounds.y + bounds.height);
        screenHandler.clearSpace(startX, (DrawingArea.WINDOW_COLUMNS - startX),
                yOffset, yOffset + mapYRange*4 - 2);
        List<MyPair<Point, Sprite>> filterObjects = null;
        if (filter != null) {
            filterObjects = filter.getObjects(model);
        }

        int row = 0;
        for (int y = yVals.from; y <= yVals.to; ++y) {
            int col = 0;
            for (int x = xVals.from; x <= xVals.to; ++x) {
                int screenX = startX + 4*col;
                int y_extra = 2 * (1 - (x % 2));
                int screenY = yOffset - 2 + 4 * row + y_extra;

                drawHex(model, x, y, screenX, screenY, partyPosition, mapYRange, yOffset, getFlagFor(model, hexes[x][y]),
                        model.getMapObjects(new Point(x, y)));
                if (x == partyPosition.x && y == partyPosition.y &&
                        model.getParty().getLeader() != null && avatarEnabled) {
                    Sprite avatar = model.getParty().getLeader().getAvatarSprite();
                    if (alternativeAvatar != null) {
                        avatar = alternativeAvatar;
                    }
                    int bottomAlignedYpos = screenY - avatar.getHeight() / 8 + 4;
                    screenHandler.register(avatar.getName(), new Point(screenX, bottomAlignedYpos), avatar, 3);
                }
                if ((cursorPos == null && (x == viewPoint.x && y == viewPoint.y)) ||
                        (cursorPos != null && (x == cursorPos.x && y == cursorPos.y))) {
                    cursor.updateYourself(screenHandler, screenX, screenY);
                }
                model.getMainStory().drawMapObjects(model, x, y, screenX, screenY);
                drawDestinationTasks(model, x, y, screenX, screenY);
                drawFilterObjects(model, filterObjects, x, y, screenX, screenY);
                if (filter instanceof WaterPathDistancesFilter) {
                    drawWaterPaths(screenHandler, x, y, screenX, screenY);
                }
                col++;
            }
            row++;
        }
    }

    private void drawWaterPaths(ScreenHandler screenHandler, int x, int y, int screenX, int screenY) {
        for (WaterPath p : hexes[x][y].getWaterPaths()) {
            if (p.getHex() == hexes[x][y]) {
                String str = String.format("%X", p.getDistance());
                char dist = p.isDistanceUnset() ? 'X' : (str).charAt(0);
                if (p.getDistance() > 15) {
                    dist = '*';
                }
                int finalX = screenX;
                int finalY = screenY;

                switch (p.getDirection()) {
                    case Direction.NORTH:
                        finalX += 1;
                        break;
                    case Direction.NORTH_EAST:
                        finalX += 3;
                        finalY += 1;
                        break;
                    case Direction.SOUTH_EAST:
                        finalX += 3;
                        finalY += 3;
                        break;
                    case Direction.SOUTH:
                        finalX += 1;
                        finalY += 3;
                        break;
                    case Direction.SOUTH_WEST:
                        finalY += 3;
                    default:
                }

                screenHandler.register("sdas", new Point(finalX, finalY),
                        CharSprite.make(dist, MyColors.LIGHT_RED));
            }
        }
    }

    private void drawFilterObjects(Model model, List<MyPair<Point, Sprite>> filterObjects, int x, int y, int screenX, int screenY) {
        if (filterObjects != null) {
            for (MyPair<Point, Sprite> pair : filterObjects) {
                if (x == pair.first.x && y == pair.first.y) {
                    model.getScreenHandler().register(pair.second.getName(), new Point(screenX, screenY), pair.second);
                }
            }
        }
    }

    private void drawDestinationTasks(Model model, int x, int y, int screenX, int screenY) {
        for (DestinationTask dt : model.getParty().getDestinationTasks()) {
            Point pos = dt.getPosition();
            if (dt.drawTaskOnMap(model) && pos != null && pos.x == x && pos.y == y && !dt.isCompleted() && !dt.isFailed(model)) {
                model.getScreenHandler().register(DESTINATION_SPRITE.getName(), new Point(screenX, screenY), DESTINATION_SPRITE);
            }
        }
    }

    private int getFlagFor(Model model, WorldHex hex) {
        if (hex.getLocation() == null || !(hex.getLocation() instanceof UrbanLocation)) {
            return HexLocation.FLAG_NONE;
        }
        if (FlagPoleNode.showSuccessFlag(model, (UrbanLocation) hex.getLocation())) {
            return HexLocation.FLAG_SUCCESS;
        }
        return HexLocation.FLAG_NONE;
    }

    protected void drawHex(Model model, int x, int y, int screenX, int screenY,
                           Point partyPosition, int mapYRange, int yOffset, int flag, List<MapObject> mapObjects) {
        ScreenHandler screenHandler = model.getScreenHandler();
        if (hexes[x][y] != null) {
            int mask = 0x0000000F - currentState;

            if ((hexes[x][y].getState() & mask) == 0 ) {
                if (screenY == yOffset - 2) {
                    hexes[x][y].drawLowerHalf(screenHandler, screenX, screenY);
                    MyLists.forEach(mapObjects, (MapObject mo) -> mo.drawLowerHalf(screenHandler, screenX, screenY+2));
                } else if (screenY == yOffset - 2 + 4 * (mapYRange - 1) + 2) {
                    hexes[x][y].drawUpperHalf(screenHandler, screenX, screenY, flag);
                    MyLists.forEach(mapObjects, (MapObject mo) -> mo.drawUpperHalf(screenHandler, screenX, screenY));
                } else {
                    hexes[x][y].drawYourself(screenHandler, screenX, screenY, flag);
                    MyLists.forEach(mapObjects, (MapObject mo) -> mo.drawYourself(screenHandler, screenX, screenY));
                }
            }
        }
    }

    private static Interval calcInterval(int t, int mapRange, int min, int max) {
        int tMin = t - mapRange/2;
        int tMax = t + mapRange/2 - 1;
        if (tMin < min) {
            tMin = min;
            tMax = min + mapRange - 1;
        } else if (tMax >= max) {
            tMax = max - 1;
            tMin = max - mapRange;
        }
        return new Interval(tMin, tMax);
    }

    public WorldHex getHex(Point position) {
        return hexes[position.x][position.y];
    }


    public void move(Point position, int dx, int dy) {
        if (position.x == bounds.x && dx < 0) {
            dx = 0;
        }
        if (position.x == bounds.x + bounds.width - 1 && dx > 0) {
            dx = 0;
        }
        if (position.y == bounds.y && dy < 0) {
            dy = 0;
        }
        if (position.y == bounds.y + bounds.height - 1 && dy > 0) {
            dy = 0;
        }
        position.x += dx;
        position.y += dy;
        if (position.y == 0 && position.x % 2 == 1) {
            position.y = 1;
        }
        if (position.y == WorldBuilder.WORLD_HEIGHT - 1 && position.x % 2 == 0) {
            position.y = WorldBuilder.WORLD_HEIGHT - 2;
        }
    }

    public List<UrbanLocation> getLordLocations() {
        List<UrbanLocation> result = new ArrayList<UrbanLocation>();
        for (int y = 0; y < hexes[0].length; ++y) {
            for (int x = 0; x < hexes.length; ++x) {
                if (hexes[x][y].hasLord()) {
                    result.add((UrbanLocation)(hexes[x][y].getLocation()));
                }
            }
        }
        return result;
    }

    public boolean canTravelTo(Model model, Point p) {
        Rectangle bounds;
        if (type == WorldType.original) {
            bounds = WorldBuilder.getWorldBounds(getCurrentState());
        } else {
            bounds = this.bounds;
        }
        if (p.x < bounds.x || p.x >= bounds.x + bounds.width) {
            return false;
        }
        if (p.y < bounds.y || p.y >= bounds.y + bounds.height) {
            return false;
        }
        return getHex(p).canTravelTo(model) || model.isInCaveSystem();
    }

    public boolean travelingAlongRoad(Point position, Point previousPosition, int direction) {
        return getHex(previousPosition).getRoadInDirection(direction) &&
                getHex(position).getRoadInDirection(Direction.opposite(direction));
    }

    public TownLocation getTownByName(String townName) {
        for (int y = 0; y < hexes[0].length; ++y) {
            for (int x = 0; x < hexes.length; ++x) {
                if (hexes[x][y].getLocation() != null) {
                    if (hexes[x][y].getLocation().getName().contains(townName)) {
                        return (TownLocation) hexes[x][y].getLocation();
                    }
                }
            }
        }
        throw new IllegalArgumentException("No town found for \"" + townName + "\"");
    }

    public UrbanLocation getUrbanLocationByPlaceName(String placeName) {
        for (int y = 0; y < hexes[0].length; ++y) {
            for (int x = 0; x < hexes.length; ++x) {
                if (hexes[x][y].getLocation() instanceof UrbanLocation) {
                    UrbanLocation urb = (UrbanLocation) (hexes[x][y].getLocation());
                    if (urb.getPlaceName().contains(placeName)) {
                        return urb;
                    }
                }
            }
        }
        throw new IllegalArgumentException("No town found for \"" + placeName + "\"");
    }

    public HexLocation getLocationByName(String name) {
        for (int y = 0; y < hexes[0].length; ++y) {
            for (int x = 0; x < hexes.length; ++x) {
                if (hexes[x][y].getLocation() != null) {
                   if (hexes[x][y].getLocation().getName().equals(name)) {
                       return hexes[x][y].getLocation();
                   }
                }
            }
        }
        throw new IllegalArgumentException("No hex location found for \"" + name + "\"");
    }

    public Point getPositionForHex(WorldHex hex) {
        for (int y = 0; y < hexes[0].length; ++y) {
            for (int x = 0; x < hexes.length; ++x) {
                if (hexes[x][y] == hex) {
                    return new Point(x, y);
                }
            }
        }
        throw new IllegalArgumentException("Hex not found in world.");
    }

    public void dijkstrasBySea(WorldHex startingHex) {
        clearWaterPaths();
        List<WaterPath> currentSet = new ArrayList<>(startingHex.getWaterPaths());
        int distance = 0;
        while (!currentSet.isEmpty()) {
            List<WaterPath> nextSet = new ArrayList<>();
            for (WaterPath p : currentSet) {
                p.setDistance(distance);

                for (WaterPath p2 : p.getHex().getWaterPaths()) {
                    if (!nextSet.contains(p2) && p2.isDistanceUnset()) {
                        nextSet.add(p2);
                    }
                }

                if (p.getOtherHex() != null) {
                    for (WaterPath p2 : p.getOtherHex().getWaterPaths()) {
                        if (!nextSet.contains(p2) && p2.isDistanceUnset()) {
                            nextSet.add(p2);
                        }
                    }
                }
            }
            distance++;
            currentSet = nextSet;
        }
    }

    public void clearWaterPaths() {
        for (WaterPath p : waterWays) {
            p.clearDistance();
        }
    }

    public Point findClosestWaterPath(Point currentPos) {
        WaterPath cheapest = null;
        int cost = 100000;
        for (WaterPath p : hexes[currentPos.x][currentPos.y].getWaterPaths()) {
            if (p.getDistance() < cost) {
                cheapest = p;
                cost = p.getDistance();
            }
        }
        if (cheapest == null) {
            throw new IllegalStateException("Could not find cheapest water path!");
        }

        WorldHex otherHex = cheapest.getOtherHex();
        if (cheapest.getOtherHex() == null) {
            throw new IllegalStateException("Could not find other hex.");
        }

        cost = 100000;
        WaterPath otherCheapest = null;
        for (WaterPath p : otherHex.getWaterPaths()) {
            if (p.getDistance() < cost) {
                otherCheapest = p;
                cost = p.getDistance();
            }
        }

        if (otherCheapest == null) {
            throw new IllegalStateException("Could not find cheapest for other hex.");
        }

        if (otherCheapest.getDistance() < cheapest.getDistance()) {
            return getPositionForHex(otherHex);
        }
        return getPositionForHex(cheapest.getHex());
    }

    public void setAlternativeAvatar(Sprite sprite) {
        alternativeAvatar = sprite;
    }

    public void dijkstrasByLand(Point position) {
        dijkstrasByLand(position, true);
    }

    public void dijkstrasByLand(Point position, boolean allowSeaHexes) {
        landNodes = new HashMap<>();
        Set<Point> currentSet = new HashSet<>();
        Set<Point> nextSet = new HashSet<>();
        int distance = 0;
        currentSet.add(position);
        while (!currentSet.isEmpty()) {
            for (Point p : currentSet) {
                landNodes.put(getHex(p), distance);
            }
            for (Point p : currentSet) {
                for (Point dxdys : Direction.getDxDyDirections(p)) {
                    Point neighbor = new Point(p);
                    move(neighbor, dxdys.x, dxdys.y);
                    if (!p.equals(neighbor) && !landNodes.containsKey(getHex(neighbor)) && !nextSet.contains(neighbor)) {
                        if (!(getHex(neighbor) instanceof SeaHex) || allowSeaHexes) {
                            nextSet.add(neighbor);
                        }
                    }
                }
            }
            currentSet.clear();
            currentSet.addAll(nextSet);
            nextSet.clear();
            distance++;
        }
    }

    public List<Point> generalShortestPath(int rank, Predicate<WorldHex> pred) {
        List<MyPair<Integer, Point>> candidates = new ArrayList<>();
        for (WorldHex hex : landNodes.keySet()) {
            if (pred.test(hex)) {
                candidates.add(new MyPair<>(landNodes.get(hex), getPositionForHex(hex)));
            }
        }
        Collections.sort(candidates, (p1, p2) -> p1.first - p2.first);
        List<Point> path = new ArrayList<>();
        path.add(candidates.get(rank).second);
        if (path.isEmpty()) {
            throw new IllegalStateException("Can't find town or castle!");
        }
        Point current = path.get(0);
        while (landNodes.get(getHex(current)) > 0) {
            int distToCurrent = landNodes.get(getHex(current));
            for (Point dxdy : Direction.getDxDyDirections(current)) {
                Point neighbor = new Point(current);
                move(neighbor, dxdy.x, dxdy.y);
                if (landNodes.containsKey(getHex(neighbor)) &&
                        landNodes.get(getHex(neighbor)) < distToCurrent) {
                    path.add(0, neighbor);
                    current = neighbor;
                    break;
                }
            }
        }
        return path;
    }

    public List<Point> shortestPathToNearestTownOrCastle(int rank) {
        return generalShortestPath(rank,
                (WorldHex hex) -> hex.getLocation() != null &&
                        (hex.getLocation() instanceof TownLocation ||
                         hex.getLocation() instanceof CastleLocation));
    }

    public List<Point> shortestPathToNearestTownOrCastle() {
        return shortestPathToNearestTownOrCastle(0);
    }

    public List<Point> shortestPathToNearestRuins() {
        return generalShortestPath(0,
                (WorldHex hex) -> hex.getLocation() != null && hex.getLocation() instanceof RuinsLocation);
    }

    public List<Point> shortestPathToNearestTemple() {
        return generalShortestPath(0,
                (WorldHex hex) -> hex.getLocation() != null && hex.getLocation() instanceof TempleLocation);
    }

    public List<Point> shortestPathToNearestInn() {
        return generalShortestPath(0,
                (WorldHex hex) -> hex.getLocation() != null && hex.getLocation() instanceof InnLocation);
    }

    public List<RuinsLocation> getRuinsLocations() {
        List<RuinsLocation> result = new ArrayList<>();
        for (int y = 0; y < hexes[0].length; ++y) {
            for (int x = 0; x < hexes.length; ++x) {
                if (hexes[x][y].getLocation() instanceof RuinsLocation) {
                    result.add((RuinsLocation)(hexes[x][y].getLocation()));
                }
            }
        }
        return result;
    }

    public List<TempleLocation> getTempleLocations() {
        List<TempleLocation> result = new ArrayList<>();
        for (int y = 0; y < hexes[0].length; ++y) {
            for (int x = 0; x < hexes.length; ++x) {
                if (hexes[x][y].getLocation() instanceof TempleLocation) {
                    result.add((TempleLocation)(hexes[x][y].getLocation()));
                }
            }
        }
        return result;
    }

    public CastleLocation getCastleByName(String castleName) {
        for (int y = 0; y < hexes[0].length; ++y) {
            for (int x = 0; x < hexes.length; ++x) {
                if (hexes[x][y].getLocation() != null) {
                    if (hexes[x][y].getLocation().getName().contains(castleName)) {
                        return (CastleLocation) hexes[x][y].getLocation();
                    }
                }
            }
        }
        throw new IllegalArgumentException("No castle found for argument " + castleName);
    }

    public int getCurrentState() {
        return currentState;
    }

    public void setCurrentState(int currState) {
        this.currentState = currState;
        this.bounds = WorldBuilder.getWorldBounds(currState);
        makeWaterWays();
    }

    public Point getPositionForLocation(HexLocation location) {
        for (int y = 0; y < hexes[0].length; ++y) {
            for (int x = 0; x < hexes.length; ++x) {
                if (hexes[x][y].getLocation() != null) {
                    if (hexes[x][y].getLocation() == location) {
                        return new Point(x, y);
                    }
                }
            }
        }
        throw new IllegalArgumentException("No position found for argument " + location);
    }

    public List<Point> shortestPathToPoint(Point first) {
        return generalShortestPath(0, worldHex -> {
            Point p = getPositionForHex(worldHex);
            return p.x == first.x && p.y == first.y;
        });
    }

    public Point getRandomPositionWithinBounds() {
        return new Point(MyRandom.randInt(bounds.x, bounds.x+bounds.width-1),
                MyRandom.randInt(bounds.y, bounds.y+bounds.height-1));
    }

    public Map<CastleLocation, List<Point>> getKingdoms() {
        return kingdoms;
    }

    public CastleLocation getKingdomForPosition(Point position) {
        CastleLocation kingdomCastle = null;
        for (CastleLocation castle : kingdoms.keySet()) {
            if (MyLists.any(kingdoms.get(castle), p -> p.equals(position))) {
                kingdomCastle = castle;
                break;
            }
        }
        return kingdomCastle;
    }

    private static class Interval {
        public int from;
        public int to;

        public Interval(int xMin, int xMax) {
            from = xMin;
            to = xMax;
        }
    }

    private void makeWaterWays() {
        System.out.print(", Making waterways");
        int[] directions = new int[]{Direction.NORTH, Direction.NORTH_EAST, Direction.SOUTH_EAST,
                Direction.SOUTH, Direction.SOUTH_WEST, Direction.NORTH_WEST};
        Set<WaterPath> paths = new HashSet<>();
        for (int y = 0; y < hexes[0].length; ++y) {
            for (int x = 0; x < hexes.length; ++x) {
                hexes[x][y].getWaterPaths().clear();
                for (int dir : directions) {
                    if ((hexes[x][y].getRivers() & dir) != 0) {
                        WaterPath p = getOppositeWaterPath(paths, x, y, dir);
                        if (p == null) {
                            p = new WaterPath(hexes[x][y], dir);
                            paths.add(p);
                        } else {
                            p.setOtherHex(hexes[x][y]);
                        }
                        hexes[x][y].addWaterPath(p);
                    }
                }
            }
        }
        waterWays = paths;
        System.out.println(" - done");
    }

    private WaterPath getOppositeWaterPath(Set<WaterPath> paths, int x, int y, int dir) {
        int opDir = Direction.directionForName(Direction.opposite(Direction.nameForDirection(dir)));
        List<Point> directions = Direction.getDxDyDirections(new Point(x, y));
        java.util.List<String> shorts = List.of("SE", "S", "SW", "NW", "N", "NE");
        Point direction = directions.get(shorts.indexOf(Direction.nameForDirection(dir)));

        for (WaterPath wp : paths) {
            Point current = new Point(x, y);
            move(current, direction.x, direction.y);
            Point target = getPositionForHex(wp.getHex());
            if (wp.getDirection() == opDir && current.x == target.x && current.y == target.y) {
                //System.out.println("Found opposing water path at " + x + "," + y);
                return wp;
            }
        }
        return null;
    }


    private Map<CastleLocation, List<Point>> findKingdoms() {
        Map<CastleLocation, List<Point>> result = new HashMap<>();
        for (int y = 0; y < hexes[0].length; ++y) {
            for (int x = 0; x < hexes.length; ++x) {
                if (hexes[x][y].getLocation() instanceof CastleLocation) {
                    CastleLocation castle = (CastleLocation)hexes[x][y].getLocation();
                    List<Point> pointsForKingdom = new ArrayList<>(castle.getExtraKingdomPositions());
                    HashSet<Point> set = new HashSet<>();
                    addPointsAround(set, x, y, 5);
                    pointsForKingdom.addAll(set);
                    result.put(castle, pointsForKingdom);
                }
            }
        }
        return result;
    }

    private void addPointsAround(HashSet<Point> set, int x, int y, int level) {
        if (!(hexes[x][y] instanceof SeaHex)) {
            set.add(new Point(x, y));
        }
        if (level > 0) {
            for (Point dxdy : Direction.getDxDyDirections(new Point(x, y))) {
                addPointsAround(set, x + dxdy.x, y + dxdy.y, level-1);
            }
        }
    }
}
