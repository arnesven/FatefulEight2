package model.states.mine;

import model.Model;
import model.SteppingMatrix;
import util.MyPair;
import util.MyRandom;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.List;

public class LogicalMine {
    // Directions in the mine:
    // 0 => NORTH, 1 => WEST, 2 => EAST, 3 => SOUTH

    private final Random random;
    private Point startPoint;
    private Map<String, MineRoom> rooms = new HashMap<>();
    private MineRoomLocation currentLocation;
    private MineRoom currentRoom;

    public LogicalMine() {
        this.random = new Random(1234);
        currentLocation = new MineRoomLocation(0, 0, 1);

        // First room
        this.currentRoom = MineRoom.makeBasicRoom(random, currentLocation.level);
        currentRoom.makeExit(random);
        startPoint = currentRoom.getStartingPoint();
        rooms.put(currentLocation.asString(), currentRoom);

        // Room on other side of mine exit (missing one connection)
        MyPair<MineRoom, MineRoomLocation> pair = currentRoom.makeAntiRoom(random, currentLocation.level);
        rooms.put(pair.second.asString(), pair.first);
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
        if (!rooms.containsKey(currentLocation.asString())) {
            rooms.put(currentLocation.asString(), MineRoom.makeBasicRoom(random, currentLocation.level));
        }
        startPoint = new Point(currentRoom.getPositionOppositeExit(direction));
        currentRoom = rooms.get(currentLocation.asString());
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
