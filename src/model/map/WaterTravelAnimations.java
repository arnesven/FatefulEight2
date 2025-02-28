package model.map;

import model.Model;
import util.Arithmetics;
import util.MyLists;
import view.sprites.Sprite;
import view.subviews.MapSubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WaterTravelAnimations {
/*
     N     NE
      +---+
  NW /     \
     \     / SE
      +---+
    SW     S
*/

    private static final List<Integer> DIRS_CLOCKWISE = List.of(
            Direction.SOUTH, Direction.SOUTH_WEST, Direction.NORTH_WEST,
            Direction.NORTH, Direction.NORTH_EAST, Direction.SOUTH_EAST);

    public static int animateMovementAlongWaterEdges(Model model, MapSubView mapSubView,
                                                     Point currentPos, Point viewPoint, Point closest, int innerPos,
                                                     Sprite shipAvatar) {
        Point fromPos = model.getWorld().translateToScreen(currentPos, viewPoint,
                MapSubView.MAP_WIDTH_HEXES, MapSubView.MAP_HEIGHT_HEXES);

        java.util.List<Integer> interPath = findInterPath(model, currentPos, closest, innerPos);
        System.out.println("Resulting path is: " + MyLists.commaAndJoin(interPath,
                Direction::getLongNameForDirection));
        for (int i = 0; i < interPath.size() - 1; ++i) {
            int dir = interPath.get(i);
            int nextDir = interPath.get(i + 1);
            Point fromPos2 = waterOffset(fromPos, dir);
            Point toPos = waterOffset(fromPos, nextDir); // closest
            mapSubView.addMovementAnimation(shipAvatar, fromPos2, toPos);
            mapSubView.waitForAnimation();
            mapSubView.removeMovementAnimation();
        }
        return adjustNextPosition(interPath.get(interPath.size()-1));
    }

    public static int findWaterDirection(Model model, Point currentPos) {
        WorldHex from = model.getWorld().getHex(currentPos);
        for (Integer dir : DIRS_CLOCKWISE) {
            if (from.getRiversInDirection(dir)) {
                return dir;
            }
        }
        throw new FaultyWaterTravelException("No water found to travel from.");
    }

    private static int adjustNextPosition(Integer dir) {
        return Direction.opposite(nextClockwiseDirection(dir));
    }

    private static int nextClockwiseDirection(Integer dir) {
        int index = DIRS_CLOCKWISE.indexOf(dir);
        index = Arithmetics.incrementWithWrap(index, DIRS_CLOCKWISE.size());
        return DIRS_CLOCKWISE.get(index);
    }

    private static int nextCounterClockwiseDir(Integer dir) {
        int index = DIRS_CLOCKWISE.indexOf(dir);
        index = Arithmetics.decrementWithWrap(index, DIRS_CLOCKWISE.size());
        return DIRS_CLOCKWISE.get(index);
    }

    private static java.util.List<Integer> findInterPath(Model model, Point currentPos, Point destination, int innerPos) {
        System.out.println("   ---");
        System.out.println("Going from " + currentPos + " to " + destination);
        System.out.println("Current inner position: " + Direction.getLongNameForDirection(innerPos));
        WorldHex from = model.getWorld().getHex(currentPos);
        Point dxdy = findDxDy(model, currentPos, destination);
        System.out.println("Dx dy is: " + dxdy);
        int targetDirection = Direction.getDirectionForDxDy(currentPos, dxdy);
        System.out.println("Target inner position: " + Direction.getLongNameForDirection(targetDirection));

        java.util.List<Integer> result1 = findInnerPathClockwise(from, innerPos, targetDirection);
        java.util.List<Integer> result2 = findInnerPathCounterClockwise(from, innerPos, targetDirection);
        if (result1 == null && result2 == null) {
            throw new FaultyWaterTravelException("Can't find inner path!");
        }
        if (result1 == null) {
            return result2;
        }
        if (result2 == null || result1.size() < result2.size()) {
            return result1;
        }
        return result2;
    }

    private static java.util.List<Integer> findInnerPathClockwise(WorldHex from, int innerPos, int targetDirection) {
        int currentIndex = DIRS_CLOCKWISE.indexOf(innerPos);
        java.util.List<Integer> result = new ArrayList<>();
        int currentDir = DIRS_CLOCKWISE.get(currentIndex);
        result.add(currentDir);
        for (int i = 0; i < 5; ++i) {
            if (currentDir == targetDirection) {
                break;
            }
            System.out.println("Testing rivers in direction: " + Direction.getLongNameForDirection(currentDir));
            if (!from.getRiversInDirection(currentDir)) {
                System.out.println("   No river - aborting.");
                return null;
            }

            currentIndex = Arithmetics.incrementWithWrap(currentIndex, DIRS_CLOCKWISE.size());
            currentDir = DIRS_CLOCKWISE.get(currentIndex);
            result.add(currentDir);
        }
        return result;
    }

    private static java.util.List<Integer> findInnerPathCounterClockwise(WorldHex from, int innerPos, int targetDirection) {
        int currentIndex = DIRS_CLOCKWISE.indexOf(innerPos);
        List<Integer> result = new ArrayList<>();
        int currentDir = DIRS_CLOCKWISE.get(currentIndex);
        result.add(currentDir);
        for (int i = 0; i < 5; ++i) {
            if (currentDir == targetDirection) {
                break;
            }
            int nextDir = nextCounterClockwiseDir(currentDir);
            System.out.println("Testing rivers in direction: " + Direction.getLongNameForDirection(nextDir));
            if (!from.getRiversInDirection(nextDir)) {
                System.out.println("   No river - aborting.");
                return null;
            }
            result.add(nextDir);
            currentDir = nextDir;
        }
        return result;
    }

    private static Point findDxDy(Model model, Point currentPos, Point closest) {
        for (Point p : Direction.getDxDyDirections(currentPos)) {
            Point p2 = new Point(currentPos);
            model.getWorld().move(p2, p.x, p.y);
            if (p2.x == closest.x && p2.y == closest.y) {
                return p;
            }
        }
        throw new FaultyWaterTravelException("Could not find dxdy for pair of points");
    }

    private static Point waterOffset(Point currentPos, int dir) {
        int size = 4;
        switch (dir) {
            case Direction.NORTH:
                return new Point(currentPos.x - size/2, currentPos.y - size);
            case Direction.NORTH_WEST:
                return new Point(currentPos.x - 3*size/4, currentPos.y - size/2);
            case Direction.SOUTH_WEST:
                return new Point(currentPos.x - size/2, currentPos.y);
            case Direction.SOUTH:
                return new Point(currentPos.x + size/2, currentPos.y);
            case Direction.SOUTH_EAST:
                return new Point(currentPos.x + 3*size/4, currentPos.y - size/2);
            case Direction.NORTH_EAST:
                return new Point(currentPos.x + size/2, currentPos.y - size);
        }
        return currentPos;
    }

    public static class FaultyWaterTravelException extends IllegalStateException {
        public FaultyWaterTravelException(String s) {
            super(s);
        }
    }
}
