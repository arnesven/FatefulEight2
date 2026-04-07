package model.states.mine;

import model.Model;
import model.SteppingMatrix;
import util.MyPair;

import java.awt.*;
import java.util.Map;
import java.util.Random;

public class LogicalMine {

    private final Random random;
    private Point startPoint;
    private final MineRoomMap rooms = new MineRoomMap();
    private final MineRoomLocation currentLocation;
    private MineRoom currentRoom;

    public LogicalMine() {
        this.random = new Random(1234);
        currentLocation = new MineRoomLocation(0, 0, 1);

        // First room
        this.currentRoom = MineRoom.makeStartingRoom(random, currentLocation.level);
        startPoint = currentRoom.getExitPosition();
        rooms.put(currentLocation, currentRoom);
        rooms.setDiscovered(currentRoom);

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

    public void moveToRoom(MineDirection direction) {
        currentLocation.moveInDirection(direction);
        if (!rooms.roomExists(currentLocation)) {
            MineRoom newRoom = MineRoom.makeConnectingRoom(random, currentLocation, rooms, currentRoom, direction);
            rooms.put(currentLocation, newRoom);
        }
        currentRoom = rooms.get(currentLocation);
        rooms.setDiscovered(currentRoom);
        startPoint = new Point(currentRoom.getConnector(direction.getOpposite()));
    }

    public MineRoomLocation getCurrentLocation() {
        return currentLocation;
    }

    public boolean roomIsDiscovered(MineRoomLocation loc) {
        return rooms.isRoomDiscovered(loc);
    }

    public void destroyRock(Model model, MineableObject mineObject) {
        Point position = getPositionFor(mineObject);
        getMatrix().remove(mineObject);
        getMatrix().addElement(position.x, position.y, new RockDebrisObject(mineObject.getDebrisSprite()));
    }

    public Point getPositionFor(MineableObject mineObject) {
        return getMatrix().getPositionFor(mineObject);
    }
}
