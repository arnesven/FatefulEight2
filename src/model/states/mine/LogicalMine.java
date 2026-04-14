package model.states.mine;

import model.Model;
import model.SteppingMatrix;
import model.combat.CombatAdvantage;
import model.states.CombatEvent;
import util.MyPair;
import util.MyRandom;
import util.MyStrings;
import util.MyTriplet;
import view.combat.CaveTheme;
import view.subviews.MineSubView;

import java.awt.*;
import java.util.Random;

public class LogicalMine {

    private final Random random;
    private final ElevatorMineObject elevator;
    private final boolean enteredFromSurface;
    private Point startPoint;
    private final MineRoomMap rooms = new MineRoomMap();
    private final MineRoomLocation currentLocation;
    private MineRoom currentRoom;

    public LogicalMine(boolean enteredFromSurface) {
        this.enteredFromSurface = enteredFromSurface;
        this.random = new Random();
        int startingLevel = enteredFromSurface ? 1 : random.nextInt(3, 6);
        currentLocation = new MineRoomLocation(0, 0, startingLevel);
        // First room
        this.currentRoom = MineRoom.makeStartingRoom(this, random, currentLocation.level);
        startPoint = currentRoom.getExitPosition();
        rooms.put(currentLocation, currentRoom);
        rooms.setDiscovered(currentRoom);

        MyTriplet<MineRoom, MineRoomLocation, ElevatorMineObject> triple = currentRoom.makeAntiRoom(this, random, currentLocation.level);
        rooms.put(triple.second, triple.first);
        this.elevator = triple.third;
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
            MineRoom newRoom = MineRoom.makeConnectingRoom(this, random, currentLocation, rooms, currentRoom, direction);
            rooms.put(currentLocation, newRoom);
        }
        currentRoom = rooms.get(currentLocation);
        rooms.setDiscovered(currentRoom);
        startPoint = new Point(currentRoom.getConnector(direction.getOpposite()));
    }

    public void moveWithElevator(int destinationLevel) {
        currentLocation.level = destinationLevel;
        if (!rooms.roomExists(currentLocation)) {
            MineRoom newRoom = MineRoom.makeConnectingRoom(this, random, currentLocation, rooms, null, null);
            rooms.put(currentLocation, newRoom);
        }
        elevator.setLevel(destinationLevel);
        currentRoom = rooms.get(currentLocation);
        rooms.setDiscovered(currentRoom);
        startPoint = new Point(elevator.getPositionInRoom());
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

    public void addNPCs(SteppingMatrix<MineObject> matrix, int level) {

    }

    public CombatEvent combatIsTriggered(Model model, AdvancedMineEvent state, MineSubView subView, Point previousPosition) {
        Point currentPosition = subView.getAvatarPosition();
        MineObject currentObj = getMatrix().getElementAt(currentPosition.x, currentPosition.y);
        if (currentObj instanceof EnemyMineObject enemyObj) {
            state.println("You encounter " + enemyObj.getDescription() + "!");
            model.getLog().waitForAnimationToFinish();
            CombatEvent combatEvent = new CombatEvent(model, enemyObj.getEnemies(),
                    new CaveTheme(), true, CombatAdvantage.Neither);
            combatEvent.run(model);
            return combatEvent;
        }

        return checkForAdjacentCombat(model, state, subView, currentPosition, previousPosition);
    }

    private CombatEvent checkForAdjacentCombat(Model model, AdvancedMineEvent state, MineSubView subView, Point currentPosition, Point previousPosition) {
        for (MineDirection dir : MineDirection.FLAT_DIRECTIONS) {
            MineRoomLocation loc = new MineRoomLocation(currentPosition.x, currentPosition.y, 0);
            loc.moveInDirection(dir);

            if (MineRoom.isWithinRoomBounds(loc.xy)) {
                MineObject adjacentObj = getMatrix().getElementAt(loc.xy.x, loc.xy.y);
                if (adjacentObj instanceof EnemyMineObject enemyAdjacentObj && enemyAdjacentObj.doesTriggerCombatFromAdjacent()) {
                    state.print("The " + enemyAdjacentObj.getDescription() + " has detected you... ");
                    if (!enemyAdjacentObj.usedSleepingPotion(model, state)) {
                        enemyAdjacentObj.moveToPlayer(subView, loc.xy);

                        Point directionOfTravel = new Point(currentPosition.x - previousPosition.x,
                                currentPosition.y - previousPosition.y);
                        System.out.println("Direction of travel: " + directionOfTravel);
                        System.out.println("Direction of enemy: " + dir.getDxDy());
                        CombatAdvantage advantage;
                        if (directionOfTravel.equals(dir.getDxDy())) {
                            state.println("It attacks you!");
                            advantage = CombatAdvantage.Neither;
                        } else {
                            state.println("It ambushes you!");
                            advantage = CombatAdvantage.Enemies;
                        }
                        model.getLog().waitForAnimationToFinish();
                        CombatEvent combatEvent = new CombatEvent(model, enemyAdjacentObj.getEnemies(),
                                new CaveTheme(), true, advantage);
                        combatEvent.run(model);
                        subView.removeMovementAnimation();
                        return combatEvent;
                    }
                }
            }
        }
        return null;
    }

    protected void placeRandomly(SteppingMatrix<MineObject> matrix, MineObject enemyObj) {
        for (int i = 0; i < 100; ++i) {
            int col = MyRandom.randInt(matrix.getColumns());
            int row = MyRandom.randInt(matrix.getRows());
            if (matrix.getElementAt(col, row) == null) {
                matrix.addElement(col, row, enemyObj);
                return;
            }
        }
        System.err.println("Could not place enemy after 100 tries.");
    }

    public boolean isInElevatorShaft(MineRoomLocation roomLocation) {
        return getElevatorObject().getLocation().xy.equals(roomLocation.xy);
    }

    public ElevatorMineObject getElevatorObject() {
        return elevator;
    }

    public boolean didEnterFromSurface() {
        return enteredFromSurface;
    }
}
