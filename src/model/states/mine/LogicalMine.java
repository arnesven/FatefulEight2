package model.states.mine;

import model.Model;
import model.SteppingMatrix;
import util.MyPair;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class LogicalMine {
    // Directions in the mine:
    // 0 => NORTH, 1 => WEST, 2 => EAST, 3 => SOUTH
    public static final int NORTH = 0;
    public static final int WEST = 1;
    public static final int EAST = 2;
    public static final int SOUTH = 3;


    private final Random random;
    private Point startPoint;
    private final MineRoomMap rooms = new MineRoomMap();
    private final MineRoomLocation currentLocation;
    private MineRoom currentRoom;

    public LogicalMine() {
        this.random = new Random(1234);
        currentLocation = new MineRoomLocation(0, 0, 1);

        // First room
        this.currentRoom = MineRoom.makeBasicRoom(random, currentLocation.level);
        currentRoom.makeExit(random);
        startPoint = currentRoom.getExitPosition();
        rooms.put(currentLocation, currentRoom);

        // Room on other side of mine exit (missing one connection)
        MyPair<MineRoom, MineRoomLocation> pair = currentRoom.makeAntiRoom(random, currentLocation.level);
        rooms.put(pair.second, pair.first);
    }

    public boolean canMoveInto(Model model, AdvancedMineEvent state, Point newPosition) {
        return currentRoom.canMoveInto(model, state, newPosition);
    }

    public SteppingMatrix<MineObject> getMatrix() {
        return currentRoom.getMatrix();
    }

    public Point getStartingPoint() {
        return startPoint;
    }

    public void moveToRoom(int direction) {
        currentLocation.moveInDirection(direction);
        if (!rooms.roomExists(currentLocation)) {
            MineRoom newRoom = MineRoom.makeConnectingRoom(random, currentLocation, rooms, currentRoom, direction);
            rooms.put(currentLocation, newRoom);
        }
        currentRoom = rooms.get(currentLocation);
        startPoint = new Point(currentRoom.getConnector(getOppositeDirection(direction)));
    }

    public static int getOppositeDirection(int direction) {
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
