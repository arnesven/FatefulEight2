package model.map;

import java.awt.*;
import java.util.List;

public class Direction {
    //         B     D
    //       ----- -----
    //      /           \
    //   A /             \ C
    //    /               \
    //    \               /
    //   G \             / E
    //      \           /
    //       ----- -----
    //         H     F


    // HGFE DCBA
    public static final int NORTH_WEST = 0x01; // 0000 0001
    public static final int NORTH = 0x0A;      // 0000 1010
    public static final int NORTH_EAST = 0x04; // 0000 0100
    public static final int SOUTH_EAST = 0x10; // 0001 0000
    public static final int SOUTH      = 0xA0; // 1010 0000
    public static final int SOUTH_WEST = 0x40; // 0100 0000
    public static final int ALL        = 0xFF; // 1111 1111
    public static final int NONE = 0;

    private static final List<String> shorts = List.of("SE", "S", "SW", "NW", "N", "NE");
    private static final List<Integer> dirList = List.of(SOUTH_EAST, SOUTH, SOUTH_WEST, NORTH_WEST, NORTH, NORTH_EAST);
    private static final List<Integer> oppoList = List.of(NORTH_WEST, NORTH, NORTH_EAST, SOUTH_EAST, SOUTH, SOUTH_WEST);

    public static String nameForDirection(int dir) {
        switch (dir) {
            case NORTH:
                return "N";
            case NORTH_EAST:
                return "NE";
            case SOUTH_EAST:
                return "SE";
            case SOUTH:
                return "S";
            case SOUTH_WEST:
                return "SW";
            case NORTH_WEST:
                return "NW";
        }
        throw new IllegalStateException("Illegal direction " + dir + ".");
    }

    public static String getLongNameForDirection(int dir) {
        switch (dir) {
            case NORTH:
                return "north";
            case NORTH_EAST:
                return "north east";
            case SOUTH_EAST:
                return "south east";
            case SOUTH:
                return "south";
            case SOUTH_WEST:
                return "south west";
            case NORTH_WEST:
                return "north west";
        }
        throw new IllegalStateException("Illegal direction " + dir + ".");
    }

    public static int directionForName(String directionName) {
        if (directionName.equals("SE")) {
            return SOUTH_EAST;
        }
        if (directionName.equals("S")) {
            return SOUTH;
        }
        if (directionName.equals("SW")) {
            return SOUTH_WEST;
        }
        if (directionName.equals("NE")) {
            return NORTH_EAST;
        }
        if (directionName.equals("N")) {
            return NORTH;
        }
        if (directionName.equals("NW")) {
            return NORTH_WEST;
        }
        throw new IllegalStateException("Illegal direction \"" + directionName + "\"");
    }

    public static String opposite(String directionName) {
        if (directionName.equals("SE")) {
            return "NW";
        }
        if (directionName.equals("S")) {
            return "N";
        }
        if (directionName.equals("SW")) {
            return "NE";
        }
        if (directionName.equals("NE")) {
            return "SW";
        }
        if (directionName.equals("N")) {
            return "S";
        }
        if (directionName.equals("NW")) {
            return "SE";
        }
        throw new IllegalStateException("Illegal direction \"" + directionName + "\"");
    }

    public static int opposite(int direction) {
        return oppoList.get(dirList.indexOf(direction));
    }

    public static List<Point> getDxDyDirections(Point position) {
        if (position.x % 2 == 0) {
            return List.of(new Point(1, 1), new Point(0, 1), new Point(-1, 1), new Point(-1, 0), new Point(0, -1), new Point(1, 0));
        }
        return List.of(new Point(1, 0), new Point(0, 1), new Point(-1, 0), new Point(-1, -1), new Point(0, -1), new Point(1, -1));
    }

    public static int getDirectionForDxDy(Point pointInWorld, Point dxDy) {
        List<Point> dxDys = getDxDyDirections(pointInWorld);
        if (dxDys.contains(dxDy)) {
            return dirList.get(dxDys.indexOf(dxDy));
        }
        throw new IllegalStateException(dxDy.toString() + " is not a valid direction for position " + pointInWorld.toString());
    }

    public static String getShortNameForDxDy(Point pointInWorld, Point dxDy) {
        List<Point> dxDys = getDxDyDirections(pointInWorld);
        if (dxDys.contains(dxDy)) {
            return shorts.get(dxDys.indexOf(dxDy));
        }
        throw new IllegalStateException(dxDy.toString() + " is not a valid direction for position " + pointInWorld.toString());
    }

    public static Point getDxDyForDirection(Point position, int direction) {
        return getDxDyDirections(position).get(dirList.indexOf(direction));
    }
}
