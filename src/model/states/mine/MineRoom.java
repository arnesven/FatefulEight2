package model.states.mine;

import model.Model;
import model.SteppingMatrix;
import model.items.Equipment;
import util.MyLists;
import util.MyPair;
import util.MyRandom;

import java.awt.*;
import java.util.*;
import java.util.List;

public class MineRoom {

    private static final int MINE_COLUMNS = 8;
    private static final int MINE_ROWS = 9;
    private static final Rectangle ROOM_BOUNDS = new Rectangle(MINE_COLUMNS, MINE_ROWS);

    private static final List<MineableObject> TOP_LEVEL_OBJECTS = List.of(
            new MaterialsOreObject(0), new SilverOreObject(0), new GoldOreObject(0),
            new AmethystGeodeObject(), new TopazGeodeObject(), new SapphireGeodeObject());
    private static final List<MineableObject> MID_LEVEL_OBJECTS = List.of(
            new SilverOreObject(1), new GoldOreObject(1), new MaterialsOreObject(1),
            new TopazGeodeObject(), new SapphireGeodeObject(), new EmeraldGeodeObject());
    private static final List<MineableObject> BOTTOM_LEVEL_OBJECTS = List.of(
            new GoldOreObject(2), new MaterialsOreObject(2), new SilverOreObject(2),
            new SapphireGeodeObject(), new EmeraldGeodeObject(), new RubyGeodeObject());

    private static final int[][] PROBABILITIES = new int[][]{
            new int[]{45, 70, 85, 95, 99},
            //        45  25  15  10   4   1
            new int[]{44, 67, 80, 92, 98},
            //        44  23  13  12   6   2
            new int[]{43, 64, 75, 89, 97},
            //        43  21  11  14   8   3
            new int[]{42, 61, 70, 86, 96},
            //        42  19   9  16  10   4
            new int[]{41, 58, 65, 83, 95},
            //        41  17   7  18  12   5
            new int[]{40, 55, 60, 80, 94},
            //        40  15   5  20  16   6
    };

    private SteppingMatrix<MineObject> matrix;
    private final Map<MineDirection, Point> connectPositions;
    private Point exitPos = null;
    private MineDirection exitDir = null;

    private MineRoom(SteppingMatrix<MineObject> matrix, Map<MineDirection, Point> connectPositions) {
        this.matrix = matrix;
        this.connectPositions = connectPositions;
    }

    private static Map<MineDirection, Point> makeRandomConnections(Random random, int level) {
        Map<MineDirection, Point> connectPositions = new HashMap<>();
        connectPositions.put(MineDirection.north, new Point(random.nextInt(MINE_COLUMNS-2)+1, 0));
        connectPositions.put(MineDirection.west, new Point(0, random.nextInt(MINE_COLUMNS-2)+1));
        connectPositions.put(MineDirection.east, new Point(MINE_COLUMNS-1, random.nextInt(MINE_ROWS-2)+1));
        connectPositions.put(MineDirection.south, new Point(random.nextInt(MINE_COLUMNS-2)+1, MINE_ROWS-1));
        Point randomVertical = new Point(random.nextInt(MINE_COLUMNS-4) + 2,
                random.nextInt(MINE_ROWS-4) + 2);
        MineDirection vertDir = level == 1 ? MineDirection.down
                : (random.nextInt(4) == 0 ? MineDirection.up : MineDirection.down);
        if (random.nextInt(4) == 0) {
            connectPositions.put(vertDir, randomVertical);
        }
        return connectPositions;
    }

    private static Map<MineDirection, Point> makeRandomConnectionsNoVertical(Random random, int level) {
        Map<MineDirection, Point> result = makeRandomConnections(random, level);
        result.remove(MineDirection.up);
        result.remove(MineDirection.down);
        return result;
    }

    public static MineRoom makeBasicRoom(LogicalMine logicalMine, Random random, int level, Map<MineDirection, Point> connectPositions) {
        SteppingMatrix<MineObject> matrix = new SteppingMatrix<>(MINE_COLUMNS, MINE_ROWS);
        fillWithRock(matrix, random, connectPositions);
        addPassages(matrix, connectPositions);
        addTunnels(matrix, random, connectPositions);
        makeTunnelWalls(matrix);
        addSupports(matrix);
        replaceRocks(matrix, random, level);
        logicalMine.addNPCs(matrix, level);
        return new MineRoom(matrix, connectPositions);
    }

    private static void fillWithRock(SteppingMatrix<MineObject> matrix, Random random,
                                     Map<MineDirection, Point> connectPositions) {
        List<Point> connections = new ArrayList<>(connectPositions.values());
        for (int y = 0; y < matrix.getRows(); ++y) {
            for (int x = 0; x < matrix.getColumns(); ++x) {
                int finalX = x;
                int finalY = y;
                boolean isOnExit = MyLists.any(connections,
                        p ->  p != null && p.x == finalX && p.y == finalY);
                if (!isOnExit) {
                    matrix.addElement(x, y, makeInitialRocks(random));
                }
            }
        }
    }

    private static void addPassages(SteppingMatrix<MineObject> matrix, Map<MineDirection, Point> connectPositions) {
        for (MineDirection dir : MineDirection.values()) {
            Point doorPos = connectPositions.get(dir);
            if (doorPos != null) {
                matrix.addElement(doorPos.x, doorPos.y, new MinePassageObject(dir));
            }
        }
    }

    private static void addTunnels(SteppingMatrix<MineObject> matrix, Random random, Map<MineDirection, Point> connectPositions) {
        List<Point> filtered = new ArrayList<>(connectPositions.values());
        if (filtered.size() == 1) {
            addTunnel(matrix, random, new Point(filtered.getFirst()),
                    new Point(random.nextInt(MINE_COLUMNS-2)+1,
                            random.nextInt(MINE_ROWS-2)+1));
        } else {
            for (int i = 0; i < filtered.size() - 1; ++i) {
                addTunnel(matrix, random, new Point(filtered.get(i)), new Point(filtered.get(i + 1)));
            }
        }
    }

    private static void addTunnel(SteppingMatrix<MineObject> matrix, Random random, Point start, Point end) {
        System.out.println("Making tunnel from " + start + " to " + end);
        int horiSteps = end.x - start.x;
        int vertiSteps = end.y - start.y;

        do {
            boolean step;
            if (horiSteps != 0 && vertiSteps != 0) {
                step = random.nextBoolean();
            } else {
                step = horiSteps != 0;
            }

            if (step) {
                int dx = (int) Math.signum(horiSteps);
                start.x += dx;
                horiSteps -= dx;
            } else {
                int dy = (int) Math.signum(vertiSteps);
                start.y += dy;
                vertiSteps -= dy;
            }
            if (start.equals(end)) {
                break;
            }
            MineObject obj = matrix.getElementAt(start.x, start.y);
            if (isRock(obj)) {
                matrix.remove(obj);
            }
        } while (true);
    }

    private static void print(SteppingMatrix<MineObject> matrix) {
        for (int y = 0; y < matrix.getRows(); ++y) {
            for (int x = 0; x < matrix.getColumns(); ++x) {
                MineObject obj = matrix.getElementAt(x, y);
                if (obj == null) {
                    System.out.print(" ");
                } else if (obj instanceof BreakableRockMineObject) {
                    System.out.print("o");
                } else if (obj instanceof UnbreakableRockObject) {
                    System.out.print("W");
                } else if (obj instanceof MinePassageObject) {
                    System.out.print(">");
                } else if (obj instanceof MineExitObject) {
                    System.out.print("E");
                } else {
                    System.out.print("?");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private static void makeTunnelWalls(SteppingMatrix<MineObject> matrix) {
        List<Rectangle> wallsFound = new ArrayList<>();
        for (int y = 0; y < matrix.getRows() - 1; ++y) {
            for (int x = 0; x < matrix.getColumns() - 1; ++x) {
                Rectangle wall = findBlockHorizontally(matrix, x, y);
                if (wall != null) {
                    wallsFound.add(wall);
                }
            }
        }

        wallsFound.sort(Comparator.comparingInt(o -> o.width * o.height));
        List<Rectangle> intersectors = new ArrayList<>();
        for (int i = 0; i < wallsFound.size(); ++i) {
            Rectangle r = wallsFound.get(i);
            for (int j = i + 1; j < wallsFound.size(); ++j) {
                Rectangle r2 = wallsFound.get(j);
                if (r.intersects(r2)) {
                    intersectors.add(r);
                    break;
                }
            }
        }
        wallsFound.removeAll(intersectors);

        for (Rectangle r : wallsFound) {
            for (int y = r.y; y < r.y + r.height; ++y) {
                for (int x = r.x; x < r.x + r.width; ++x) {
                    matrix.remove(matrix.getElementAt(x, y));
                    matrix.addElement(x, y, new MineWallObject(x, y, r));
                }
            }
        }
    }

    private static Rectangle findBlockHorizontally(SteppingMatrix<MineObject> matrix, int xStart, int yStart) {
        Rectangle size = null;
        for (int x = xStart; x < matrix.getColumns(); ++x) {
            if (matrix.getElementAt(x, yStart) instanceof UnbreakableRockObject) {
                if (size == null) {
                    size = new Rectangle(x, yStart, 1, 1);
                } else {
                    size.width++;
                }
            } else {
                break;
            }
        }
        if (size == null || size.width < 2) {
            return null;
        }

        for (int y = yStart + 1; y < matrix.getRows(); ++y) {
            if (matrix.getElementAt(xStart, y) instanceof UnbreakableRockObject) {
                size.height++;
            } else {
                break;
            }
        }

        if (size.height < 2) {
            return null;
        }

        for (int y = yStart + 1; y < yStart + size.height; ++y) {
            for (int x = xStart + 1; x < xStart + size.width; ++x) {
                if (!(matrix.getElementAt(x, y) instanceof UnbreakableRockObject)) {
                    return null;
                }
            }
        }
        return size;
    }

    private static void addSupports(SteppingMatrix<MineObject> matrix) {
        for (int y = 0; y < matrix.getRows(); ++y) {
            for (int x = 1; x < matrix.getColumns()-1; ++x) {
                MineObject obj = matrix.getElementAt(x, y);
                MineObject left = matrix.getElementAt(x-1, y);
                MineObject right = matrix.getElementAt(x+1, y);
                if (obj == null && isRock(left) && isRock(right)) {
                    matrix.addElement(x, y, new MineTunnelSupportObject());
                }
            }
        }
    }

    private static boolean isRock(MineObject obj) {
        return obj instanceof RockMineObject || obj instanceof MineWallObject;
    }

    private static void replaceRocks(SteppingMatrix<MineObject> matrix, Random random, int level) {
        int diamondChance = level * level;
        for (int y = 0; y < matrix.getRows(); ++y) {
            for (int x = 0; x < matrix.getColumns(); ++x) {
                MineObject obj = matrix.getElementAt(x, y);
                if (obj instanceof UnbreakableRockObject) {
                    MineObject replacement = getObjectForLevel(random, level, diamondChance);
                    matrix.remove(obj);
                    matrix.addElement(x, y, replacement);
                }
            }
        }
    }

    private static MineObject spawn(Random random, int intraLevel, List<MineableObject> templates) {
        int dieRoll = random.nextInt(100);
        for (int i = 0; i < PROBABILITIES[0].length; ++i) {
            if (dieRoll < PROBABILITIES[intraLevel-1][i]) {
                return templates.get(i).copy();
            }
        }
        return templates.getLast().copy();
    }

    private static MineObject getObjectForLevel(Random random, int level, int diamondChance) {
        if (level < 1) {
            throw new IllegalArgumentException("Bad level when spawning objects: " + level);
        }
        if (random.nextInt(3) > 0) {
            if (random.nextInt(3) == 0) {
                return new UnbreakableRockObject();
            }
            return new BreakableRockMineObject();
        }
        if (random.nextInt(100_000) < diamondChance) {
            return new DiamondGeodeObject();
        }
        if (level <= 6) {
            return spawn(random, level, TOP_LEVEL_OBJECTS);
        }
        if (level <= 12) {
            return spawn(random, level - 6, MID_LEVEL_OBJECTS);
        }
        return spawn(random, level - 12, BOTTOM_LEVEL_OBJECTS);
    }

    public static MineRoom makeConnectingRoom(LogicalMine mine, Random random, MineRoomLocation currentLocation, MineRoomMap map,
                                              MineRoom oldRoom, MineDirection direction) {
        System.out.println("From room:");
        print(oldRoom.matrix);
        Map<MineDirection, Point> connectPositions = makeRandomConnections(random, currentLocation.level);
        MineDirection opposDir = direction.getOpposite();
        Set<MineDirection> freeConnections = new HashSet<>(List.of(MineDirection.values()));
        freeConnections.remove(opposDir);

        for (MineDirection dir : MineDirection.values()) {
            if (dir == opposDir) { // Direction we came from, fix adjoining connection
                adjustConnectionToSame(connectPositions, oldRoom, opposDir);
            } else { // Other connection
                System.out.println("Other room in dir: " + dir);
                MineRoomLocation otherLoc = currentLocation.copy();
                otherLoc.moveInDirection(dir);
                MineDirection otherOpposDir = dir.getOpposite();
                if (map.roomExists(otherLoc)) {
                    freeConnections.remove(dir);
                    MineRoom otherRoom = map.get(otherLoc);
                    if (otherRoom.getConnector(otherOpposDir) != null) { // has a door in direction
                        adjustConnectionToSame(connectPositions, otherRoom, dir);
                    } else { // No door there, remove it in this room as well
                        connectPositions.remove(dir);
                    }
                }
            }
        }

        freeConnections.remove(MineDirection.up);
        freeConnections.remove(MineDirection.down);
        // freeConnections will be [0,3] in size
        System.out.println("Newly created room has " + freeConnections.size() + " free connections.");
        List<MineDirection> freeList = new ArrayList<>(freeConnections);
        Collections.shuffle(freeList);
        if (freeList.size() > 1 && random.nextInt(10) == 0) {
            connectPositions.remove(freeList.removeFirst());
        }
        if (freeList.size() > 1 && random.nextInt(10) > 1) {
            connectPositions.remove(freeList.removeFirst());
        }
        if (freeList.size() > 1 && random.nextInt(10) < 2) {
            connectPositions.remove(freeList.removeFirst());
        }
        return makeBasicRoom(mine, random, currentLocation.level, connectPositions);
    }

    private static void adjustConnectionToSame(Map<MineDirection, Point> connectPositions, MineRoom other, MineDirection towardOther) {
        MineDirection towardNew = towardOther.getOpposite();
        Point newConnect = connectPositions.get(towardOther);
        if (newConnect == null) {
            newConnect = new Point(other.connectPositions.get(towardNew));
            connectPositions.put(towardOther, newConnect);
        }
        if (towardNew == MineDirection.north || towardNew == MineDirection.south) {
            newConnect.x = other.connectPositions.get(towardNew).x;
        } else if (towardNew == MineDirection.west || towardNew == MineDirection.east){
            newConnect.y = other.connectPositions.get(towardNew).y;
        } else { // Vertical movement
            newConnect.x = other.connectPositions.get(towardNew).x;
            newConnect.y = other.connectPositions.get(towardNew).y;
        }
    }

    public static MineRoom makeStartingRoom(LogicalMine logicalMine, Random random, int level) {
        Map<MineDirection, Point> connections = makeRandomConnectionsNoVertical(random, level);
        MineRoom room = makeBasicRoom(logicalMine, random, level, connections);
        room.makeExit(random);
        return room;
    }

    public void makeExit(Random random) {
        this.exitDir = MyRandom.sample(List.of(MineDirection.north, MineDirection.west,
                MineDirection.east, MineDirection.south));
        this.exitPos = new Point(connectPositions.get(exitDir));
        matrix.remove(matrix.getElementAt(exitPos.x, exitPos.y));
        matrix.addElement(exitPos.x, exitPos.y, new MineExitObject(exitDir));
    }

    public Point getExitPosition() {
        return exitPos;
    }

    public MyPair<MineRoom, MineRoomLocation> makeAntiRoom(LogicalMine mine, Random random, int level) {
        Map<MineDirection, Point> exits = makeRandomConnectionsNoVertical(random, level);
        exits.remove(MineDirection.down);
        exits.remove(MineDirection.up);
        MineDirection opposDir = exitDir.getOpposite();
        exits.remove(opposDir);
        MineRoom antiRoom = makeBasicRoom(mine, random, level, exits);
        MineRoomLocation antiRoomLoc = new MineRoomLocation(0, 0, 1);
        antiRoomLoc.moveInDirection(exitDir);
        return new MyPair<>(antiRoom, antiRoomLoc);
    }

    public SteppingMatrix<MineObject> getMatrix() {
        return matrix;
    }

    public boolean canMoveInto(Model model, AdvancedMineEvent state, Point newPosition) {
        if (!isWithinRoomBounds(newPosition)) {
            return false;
        }
        MineObject obj = matrix.getElementAt(newPosition.x, newPosition.y);
        if (obj != null) {
            return obj.gotBumpedInto(model, state, newPosition);
        }
        return true;
    }

    public static boolean isWithinRoomBounds(Point newPosition) {
        return ROOM_BOUNDS.contains(newPosition);
    }

    private static MineObject makeInitialRocks(Random random) {
        if (random.nextInt(5) > 0) {
            return new UnbreakableRockObject();
        }
        return new BreakableRockMineObject();
    }

    public Point getConnector(MineDirection dir) {
        return connectPositions.get(dir);
    }
}
