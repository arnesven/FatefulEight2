package model.ruins;

import java.awt.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;

public class DungeonLevel implements Serializable {
    private static final List<Point> doorPositions = List.of(
            new Point(1, 0), new Point(3, 1), new Point(1, 3), new Point(0, 1));
    private static final List<String> directionsNames = List.of(
            "north", "east", "south", "west");
    private static final List<Point> directions = List.of(
            new Point(0, -1), new Point(1, 0), new Point(0, 1), new Point(-1, 0));
    private static final double CHEST_PREVALENCE = 0.4;
    private static final double LEVER_PREVALENCE = 0.3;
    private static final double CORPSE_PREVALENCE = 0.3;
    private static final double MONSTER_PREVALENCE = 0.1;
    private static final double LOCKED_DOOR_PREVALENCE = 0.05;
    private static final double TRAP_PREVALENCE = 0.1;

    private final Random random;
    private final int levelSize;

    private DungeonRoom[][] rooms;
    private Point startingPoint;
    private Point descentPoint;


    public DungeonLevel(Random random, boolean firstLevel, int levelSize) {
        this.random = random;
        this.levelSize = levelSize;
        while (true) {
            rooms = new DungeonRoom[levelSize][levelSize];
            if (buildRandomLevel(firstLevel)) {
                break;
            } else {
                System.err.println("Unsuccessfully built dungeon level");
            }
        }
    }

    protected boolean buildRandomLevel(boolean firstLevel) {
        makeBasicLevelLayout();
        putInEntryAndExit(firstLevel);
        Set<DungeonRoom> visitedRooms = new HashSet<>();
        visitedRooms.add(rooms[startingPoint.x][startingPoint.y]);
        boolean[] foundPath = new boolean[]{false};
        depthFirst(startingPoint, descentPoint, visitedRooms, foundPath);
        if (!foundPath[0]) {
            System.err.println("No path fround from entry to exit!");
            return false;
        }
        addChestAndLevers(visitedRooms);
        addMonstersAndTraps(visitedRooms);
        addCrackedWalls();
        return true;
    }


    public DungeonRoom getRoom(Point position) {
        return rooms[position.x][position.y];
    }

    public Point getStartingPoint() {
        return startingPoint;
    }

    public Point getDescentPoint() {
        return descentPoint;
    }

    private void makeBasicLevelLayout() {
        rooms[levelSize/2][levelSize/2] = makeDungeonRoom();
        rooms[levelSize/2+1][levelSize/2] = makeDungeonRoom();
        rooms[levelSize/2+1][levelSize/2+1] = makeDungeonRoom();
        rooms[levelSize/2][levelSize/2+1] = makeDungeonRoom();

        int totalRooms = levelSize*levelSize - (levelSize*levelSize)/4 - 4;
        for (int i = 0; i < totalRooms;) {
            int x = random.nextInt(levelSize);
            int y = random.nextInt(levelSize);
            if (rooms[x][y] == null) {
                if ((x > 0 && rooms[x-1][y] != null) || (x < levelSize-1 && rooms[x+1][y] != null) ||
                        (y > 0 && rooms[x][y-1] != null) || (y < levelSize-1 && rooms[x][y+1] != null)) {
                    rooms[x][y] = makeDungeonRoom();
                    startingPoint = new Point(x, y);
                    i++;
                }
            }
        }
    }


    private void addMonstersAndTraps(Set<DungeonRoom> visitedRooms) {
        DungeonRoom startingRoom = getRoom(startingPoint);
        DungeonRoom endingRoom = getRoom(descentPoint);
        for (DungeonRoom room : visitedRooms) {
            if (room.getCardinality() > 1) {
                if (startingRoom != room && endingRoom != room) {
                    double roll = random.nextDouble();
                    if (roll < MONSTER_PREVALENCE) {
                        room.addObject(DungeonMonster.makeRandomEnemies(random));
                    } else if (roll < MONSTER_PREVALENCE + TRAP_PREVALENCE) {
                        DungeonTrap.makeTrap(room, random);
                    }
                }
            }
        }
    }

    private void addChestAndLevers(Set<DungeonRoom> visitedRooms) {
        for (DungeonRoom room : visitedRooms) {
            if (room.getCardinality() == 1) {
                double roll = random.nextDouble();
                if (roll < CHEST_PREVALENCE) {
                    room.addObject(new DungeonChest(random));
                } else if (roll < CHEST_PREVALENCE + LEVER_PREVALENCE) {
                    LeverObject lever = new LeverObject(random);
                    room.addObject(lever);
                    getRoom(descentPoint).connectLeverToDoor(lever);
                } else if (roll < CHEST_PREVALENCE + LEVER_PREVALENCE + CORPSE_PREVALENCE) {
                    room.addObject(new CorpseObject());
                }
            }

        }
    }

    private void putInEntryAndExit(boolean firstLevel) {
        prepareForStairs(startingPoint);
        this.descentPoint = null;
        do {
            descentPoint = new Point(random.nextInt(levelSize), random.nextInt(levelSize));
        } while (descentPoint.equals(startingPoint) || descentPoint.y == startingPoint.y - 1 || startingPoint.y == descentPoint.y - 1);

        rooms[descentPoint.x][descentPoint.y] = makeDungeonRoom();
        prepareForStairs(descentPoint);
        if (firstLevel) {
            rooms[startingPoint.x][startingPoint.y].setConnection(0, new DungeonExit());
        } else {
            rooms[startingPoint.x][startingPoint.y].setConnection(0, new StairsUp(new Point(1, 0)));
        }
        rooms[descentPoint.x][descentPoint.y].setConnection(0, new StairsDown(new Point(1, 0)));
    }

    private void depthFirst(Point currentPoint, Point descentPoint, Set<DungeonRoom> visitedRooms, boolean[] foundPath) {
        if (currentPoint.equals(descentPoint)) {
            getRoom(currentPoint).setCardinality(2);
            foundPath[0] = true;
            return;
        }
        List<Point> dirs = new ArrayList<>(directions);
        Collections.shuffle(dirs);
        int cardinalityCount = 0;
        for (Point dir : dirs) {
            Point newPoint = new Point(currentPoint.x + dir.x, currentPoint.y + dir.y);
            if (positionOk(newPoint)) {
                DungeonRoom newRoom = rooms[newPoint.x][newPoint.y];
                if (newRoom != null && !visitedRooms.contains(newRoom)) {
                    DungeonRoomConnection doorType = DungeonRoomConnection.OPEN;
                    if (random.nextDouble() < LOCKED_DOOR_PREVALENCE) {
                        doorType = DungeonRoomConnection.LOCKED;
                    }
                    cardinalityCount++;
                    connectDoor(currentPoint.x, currentPoint.y, directionsNames.get(directions.indexOf(dir)), doorType);
                    visitedRooms.add(newRoom);
                    depthFirst(newPoint, descentPoint, visitedRooms, foundPath);
                }
            }
        }
        rooms[currentPoint.x][currentPoint.y].setCardinality(cardinalityCount+1);
    }


    private void addCrackedWalls() {
        int numberToAdd = (levelSize * levelSize) / 8;
        for (int i = 0; i < numberToAdd; ++i) {
            boolean added = false;
            for (int tries = 0; tries < 100 && !added; ++tries) {
                int x = random.nextInt(levelSize);
                int y = random.nextInt(levelSize);
                if (rooms[x][y] != null) {
                    List<Point> dirs = new ArrayList<>(directions);
                    Collections.shuffle(dirs);
                    for (Point dir : dirs) {
                        Point newPoint = new Point(x + dir.x, y + dir.y);
                        if (positionOk(newPoint) && rooms[newPoint.x][newPoint.y] != null) {
                            if (rooms[x][y].getConnection(directions.indexOf(dir)) == null) {
                                connectDoor(x, y, directionsNames.get(directions.indexOf(dir)), DungeonRoomConnection.CRACKED);
                                added = true;
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean positionOk(Point p) {
        return p.x >= 0 && p.x < levelSize && p.y >= 0 && p.y < levelSize;
    }

    private void prepareForStairs(Point point) {
        if (point.y > 0) {
            rooms[point.x][point.y-1] = null;
            if (point.x > 0) {
                rooms[point.x-1][point.y] = makeDungeonRoom();
            }
            if (point.x < levelSize-1) {
                rooms[point.x+1][point.y] = makeDungeonRoom();
            }
            if (point.y < levelSize-1) {
                rooms[point.x][point.y+1] = makeDungeonRoom();
            }
        }
    }

    private DungeonRoom makeDungeonRoom() {
        DungeonRoom room = new DungeonRoom();
        if (random.nextDouble() > 0.5) {
            if (random.nextDouble() > 0.5) {
                room.addDecoration(new RoomDecoration(RoomDecoration.LOWER_LEFT, random));
            } else {
                room.addDecoration(new RoomDecoration(RoomDecoration.UPPER_LEFT, random));
            }
        } else {
            if (random.nextDouble() > 0.5) {
                room.addDecoration(new RoomDecoration(RoomDecoration.LOWER_RIGHT, random));
            } else {
                room.addDecoration(new RoomDecoration(RoomDecoration.UPPER_RIGHT, random));
            }
        }

        return room;
    }

    public void connectDoor(int x, int y, String direction, DungeonRoomConnection type) {
        DungeonRoom room = rooms[x][y];
        int index = directionsNames.indexOf(direction);
        DungeonDoor door1 =  DungeonRoomConnection.makeObject(type, doorPositions.get(index), (index % 2) == 0, direction);
        room.setConnection(index, door1);
        Point adjacentDirection = directions.get(index);
        DungeonRoom adjacentRoom = rooms[x + adjacentDirection.x][y + adjacentDirection.y];
        int oppoIndex = (index + 2) % 4;
        DungeonDoor door2 = DungeonRoomConnection.makeObject(type, doorPositions.get(oppoIndex), (oppoIndex % 2) == 0,
                directionsNames.get(oppoIndex));
        adjacentRoom.setConnection(oppoIndex, door2);
        door1.link(door2);
        door2.link(door1);
    }

    public DungeonRoom[][] getRooms() {
        return rooms;
    }

    public void setRoom(int x, int y, DungeonRoom room) {
        rooms[x][y] = room;
    }

    protected void setStartingPoint(Point startingPoint) {
        this.startingPoint = startingPoint;
    }


    public List<DungeonRoom> getRoomList() {
        List<DungeonRoom> roomList = new ArrayList<>();
        for (int y = 0; y < rooms[0].length; ++y) {
            for (int x = 0; x < rooms.length; ++x) {
                if (rooms[x][y] != null) {
                    roomList.add(rooms[x][y]);
                }
            }
        }
        return roomList;
    }
}
