package model.states.mine;

import model.Model;
import model.SteppingMatrix;
import util.MyRandom;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.List;

public class LogicalMine {

    private static final int MINE_COLUMNS = 8;
    private static final int MINE_ROWS = 9;
    private final Random random;
    private static final List<Point> EXIT_POSITIONS = List.of(
            new Point(MINE_COLUMNS/2-1, 0),            // NORTH
            new Point(0, MINE_ROWS/2),                 // WEST
            new Point(MINE_COLUMNS-1, 4),              // EAST
            new Point(MINE_COLUMNS/2-1, MINE_ROWS-1)); // SOUTH
    private Point startPoint;

    private Map<String, SteppingMatrix<MineObject>> rooms = new HashMap<>();
    private MineRoomLocation currentLocation;
    private SteppingMatrix<MineObject> currentRoom;

    public LogicalMine() {
        this.random = new Random(1234);
        currentLocation = new MineRoomLocation(0, 0, 1);

        // First room
        this.currentRoom = makeBasicRoom(currentLocation.level);
        int exitDir = MyRandom.randInt(4);
        Point exitPos = new Point(EXIT_POSITIONS.get(exitDir));
        currentRoom.remove(currentRoom.getElementAt(exitPos.x, exitPos.y));
        currentRoom.addElement(exitPos.x, exitPos.y, new MineExitObject(exitPos));
        startPoint = exitPos;
        rooms.put(currentLocation.asString(), currentRoom);

        // Room on other side of mine exit (missing one connection)
        SteppingMatrix<MineObject> antiRoom = makeBasicRoom(currentLocation.level);
        int opposDir = getOppositeDirection(exitDir);
        Point otherSideOfExit = EXIT_POSITIONS.get(opposDir);
        antiRoom.remove(antiRoom.getElementAt(otherSideOfExit.x, otherSideOfExit.y));
        MineRoomLocation antiRoomLoc = new MineRoomLocation(0, 0, 1);
        antiRoomLoc.moveInDirection(exitDir);
        rooms.put(antiRoomLoc.asString(), antiRoom);
    }



    private SteppingMatrix<MineObject> makeBasicRoom(int level) {
        SteppingMatrix<MineObject> matrix = new SteppingMatrix<>(MINE_COLUMNS, MINE_ROWS);
        for (int y = 0; y < matrix.getRows(); ++y) {
            for (int x = 0; x < matrix.getColumns(); ++x) {
                if (x != MINE_COLUMNS/2-1 && y != MINE_ROWS/2) {
                    matrix.addElement(x, y, makeRockForLevel(level));
                } else if (y != 0 && y != MINE_ROWS - 1 &&
                        y != MINE_ROWS/2 && random.nextInt(3) == 0) {
                    matrix.addElement(x, y, new MineTunnelSupportObject());
                }
            }
        }

        for (Point doorPos : EXIT_POSITIONS) {
            matrix.addElement(doorPos.x, doorPos.y, new MinePassageObject(doorPos));
        }
        return matrix;
    }

    private MineObject makeRockForLevel(int level) {
        if (MyRandom.flipCoin()) {
            return new UnbreakableRockObject();
        }
        return new BreakableRockMineObject();
    }

    public boolean canMoveInto(Model model, AdvancedMineEvent state, Point newPosition) {
        Rectangle r = new Rectangle(MINE_COLUMNS, MINE_ROWS);
        if (!r.contains(newPosition)) {
            return false;
        }
        MineObject obj = currentRoom.getElementAt(newPosition.x, newPosition.y);
        if (obj != null) {
            return obj.gotBumpedInto(model, state, newPosition);
        }
        return true;
    }

    public SteppingMatrix<MineObject> getMatrix() {
        return currentRoom;
    }

    public Point getStartingPoint() {
        return startPoint;
    }

    public void moveToRoom(int direction) {
        currentLocation.moveInDirection(direction);
        if (!rooms.containsKey(currentLocation.asString())) {
            rooms.put(currentLocation.asString(), makeBasicRoom(currentLocation.level));
        }
        currentRoom = rooms.get(currentLocation.asString());

        int oppDir = getOppositeDirection(direction);
        startPoint = new Point(EXIT_POSITIONS.get(oppDir));
    }

    private int getOppositeDirection(int direction) {
        if (direction == 0) {
            return 3;
        }
        if (direction == 1) {
            return 2;
        }
        if (direction == 2) {
            return 1;
        }
        return 0;
    }

    public MineRoomLocation getCurrentLocation() {
        return currentLocation;
    }
}
