package model.map;

import model.Model;
import util.MyPair;
import view.DrawingArea;
import view.ScreenHandler;
import view.sprites.Sprite;
import view.subviews.SubView;

import java.awt.Point;
import java.util.*;
import java.io.Serializable;

public class World implements Serializable {

    private final Set<WaterPath> waterWays;
    private Map<WorldHex, Integer> landNodes;
    //  x   y
    private WorldHex[][] hexes;
    private ViewPointMarker cursor = new HexCursorMarker();
    private Sprite alternativeAvatar = null;

    public World(WorldHex[][] hexes) {
        this.hexes = hexes;
        waterWays = makeWaterWays();
    }

    public static Point translateToScreen(Point logicPosition, Point viewPoint, int mapXRange, int mapYRange) {
        Interval xVals = calcXValues(viewPoint.x, mapXRange);
        Interval yVals = calcYValues(viewPoint.y, mapYRange);
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
        ScreenHandler screenHandler = model.getScreenHandler();
        Interval xVals = calcXValues(viewPoint.x, mapXRange);
        Interval yVals = calcYValues(viewPoint.y, mapYRange);
        int startX = (DrawingArea.WINDOW_COLUMNS - mapXRange*4)/2;
        screenHandler.clearSpace(startX, (DrawingArea.WINDOW_COLUMNS - startX),
                yOffset, yOffset + mapYRange*4 - 2);

        int row = 0;
        for (int y = yVals.from; y <= yVals.to; ++y) {
            int col = 0;
            for (int x = xVals.from; x <= xVals.to; ++x) {
                int screenX = startX + 4*col;
                int y_extra = 2 * (1 - (x % 2));
                int screenY = yOffset - 2 + 4 * row + y_extra;

                drawHex(screenHandler, x, y, screenX, screenY, partyPosition, mapYRange, yOffset);

                if (x == partyPosition.x && y == partyPosition.y &&
                        model.getParty().getLeader() != null && avatarEnabled) {
                    Sprite avatar = model.getParty().getLeader().getAvatarSprite();
                    if (alternativeAvatar != null) {
                        avatar = alternativeAvatar;
                    }
                    screenHandler.register(avatar.getName(), new Point(screenX, screenY), avatar, 1);
                }
                if ((cursorPos == null && (x == viewPoint.x && y == viewPoint.y)) ||
                    (cursorPos != null && (x == cursorPos.x && y == cursorPos.y))) {
                        cursor.updateYourself(screenHandler, screenX, screenY);
                }
                col++;
            }
            row++;
        }
    }

    protected void drawHex(ScreenHandler screenHandler, int x, int y, int screenX, int screenY,
                           Point partyPosition, int mapYRange, int yOffset) {
        if (hexes[x][y] != null) {
            if (screenY == yOffset - 2) {
                hexes[x][y].drawLowerHalf(screenHandler, screenX, screenY);
            } else if (screenY == yOffset - 2 + 4 * (mapYRange-1) + 2) {
                hexes[x][y].drawUpperHalf(screenHandler, screenX, screenY);
            } else {
                hexes[x][y].drawYourself(screenHandler, screenX, screenY);
            }
        }
    }

    private static Interval calcXValues(int x, int mapXRange) {
        int xMin = x - mapXRange/2;
        int xMax = x + mapXRange/2 - 1;
        if (xMin < 0) {
            xMin = 0;
            xMax = mapXRange - 1;
        } else if (xMax >= WorldBuilder.WORLD_WIDTH) {
            xMax = WorldBuilder.WORLD_WIDTH - 1;
            xMin = WorldBuilder.WORLD_WIDTH - mapXRange;
        }
        return new Interval(xMin, xMax);
    }

    private static Interval calcYValues(int y, int mapYRange) {
        int yMin = y - mapYRange/2;
        int yMax = y + mapYRange/2 - 1;
        if (yMin < 0) {
            yMin = 0;
            yMax = mapYRange - 1;
        } else if (yMax >= WorldBuilder.WORLD_HEIGHT) {
            yMax = WorldBuilder.WORLD_HEIGHT - 1;
            yMin = WorldBuilder.WORLD_HEIGHT - mapYRange;
        }
        return new Interval(yMin, yMax);
    }

    public WorldHex getHex(Point position) {
        return hexes[position.x][position.y];
    }


    public static void move(Point position, int dx, int dy) {
        if (position.x == 0 && dx < 0) {
            dx = 0;
        }
        if (position.x == WorldBuilder.WORLD_WIDTH - 1 && dx > 0) {
            dx = 0;
        }
        if (position.y == 0 && dy < 0) {
            dy = 0;
        }
        if (position.y == WorldBuilder.WORLD_HEIGHT - 1 && dy > 0) {
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
        if (p.x < 0 || p.x >= WorldBuilder.WORLD_WIDTH) {
            return false;
        }
        if (p.y < 0 || p.y >= WorldBuilder.WORLD_HEIGHT) {
            return false;
        }
        return getHex(p).canTravelTo(model);
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

    public List<Point> shortestPathToNearestTownOrCastle() {
        return shortestPathToNearestTownOrCastle(0);
    }

    public List<Point> shortestPathToNearestTownOrCastle(int rank) {
        List<MyPair<Integer, Point>> candidates = new ArrayList<>();
        for (WorldHex hex : landNodes.keySet()) {
            if (hex.getLocation() != null && hex.getLocation() instanceof UrbanLocation) {
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
                if (landNodes.get(getHex(neighbor)) < distToCurrent) {
                    path.add(0, neighbor);
                    current = neighbor;
                    break;
                }
            }
        }
        return path;
    }

    private static class Interval {
        public int from;
        public int to;

        public Interval(int xMin, int xMax) {
            from = xMin;
            to = xMax;
        }
    }

    private Set<WaterPath> makeWaterWays() {
        int[] directions = new int[]{Direction.NORTH, Direction.NORTH_EAST, Direction.SOUTH_EAST,
                Direction.SOUTH, Direction.SOUTH_WEST, Direction.NORTH_WEST};
        Set<WaterPath> paths = new HashSet<>();
        for (int y = 0; y < hexes[0].length; ++y) {
            for (int x = 0; x < hexes.length; ++x) {
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
        return paths;
    }

    private WaterPath getOppositeWaterPath(Set<WaterPath> paths, int x, int y, int dir) {
        int opDir = Direction.directionForName(Direction.opposite(Direction.nameForDirection(dir)));
        List<Point> directions = Direction.getDxDyDirections(new Point(x, y));
        java.util.List<String> shorts = List.of("SE", "S", "SW", "NW", "N", "NE");
        Point direction = directions.get(shorts.indexOf(Direction.nameForDirection(dir)));

        for (WaterPath wp : paths) {
            Point current = new Point(x, y);
            World.move(current, direction.x, direction.y);
            Point target = getPositionForHex(wp.getHex());
            if (wp.getDirection() == opDir && current.x == target.x && current.y == target.y) {
                //System.out.println("Found opposing water path at " + x + "," + y);
                return wp;
            }
        }
        return null;
    }
}
