package model.states.mine;

import model.Model;
import model.SteppingMatrix;
import util.MyLists;
import util.MyPair;

import java.awt.*;
import java.util.*;
import java.util.List;

public class MineRoom {

    private static final int MINE_COLUMNS = 8;
    private static final int MINE_ROWS = 9;

    private SteppingMatrix<MineObject> matrix;
    private final List<Point> connectPositions;
    private Point exitPos = null;
    private int exitDir = -1;

    private MineRoom(SteppingMatrix<MineObject> matrix, List<Point> connectPositions) {
        this.matrix = matrix;
        this.connectPositions = connectPositions;
    }

    public static MineRoom makeBasicRoom(Random random, int level) {
        return makeBasicRoom(random, level, makeRandomConnections(random));
    }

    private static List<Point> makeRandomConnections(Random random) {
        List<Point> connectPositions = new ArrayList<>();
        connectPositions.add(new Point(random.nextInt(MINE_COLUMNS-2)+1, 0));            // NORTH
        connectPositions.add(new Point(0, random.nextInt(MINE_COLUMNS-2)+1));            // WEST
        connectPositions.add(new Point(MINE_COLUMNS-1, random.nextInt(MINE_ROWS-2)+1));  // EAST
        connectPositions.add(new Point(random.nextInt(MINE_COLUMNS-2)+1, MINE_ROWS-1));  // SOUTH
        return connectPositions;
    }

    public static MineRoom makeBasicRoom(Random random, int level, List<Point> connectPositions) {
        SteppingMatrix<MineObject> matrix = new SteppingMatrix<>(MINE_COLUMNS, MINE_ROWS);
        fillWithRock(matrix, random, level, connectPositions);
        addPassages(matrix, connectPositions);
        addTunnels(matrix, random, connectPositions);
        makeTunnelWalls(matrix);
        addSupports(matrix);
        replaceRocks(matrix, random, level);
        return new MineRoom(matrix, connectPositions);
    }

    private static void fillWithRock(SteppingMatrix<MineObject> matrix, Random random,
                                     int level, List<Point> connectPositions) {
        for (int y = 0; y < matrix.getRows(); ++y) {
            for (int x = 0; x < matrix.getColumns(); ++x) {
                int finalX = x;
                int finalY = y;
                boolean isOnExit = MyLists.any(connectPositions, p -> p != null && p.x == finalX && p.y == finalY);
                if (!isOnExit) {
                    matrix.addElement(x, y, makeInitialRocks(random));
                }
            }
        }
    }

    private static void addPassages(SteppingMatrix<MineObject> matrix, List<Point> connectPositions) {
        for (int dir = 0; dir < connectPositions.size(); dir++) {
            Point doorPos = connectPositions.get(dir);
            if (doorPos != null) {
                matrix.addElement(doorPos.x, doorPos.y, new MinePassageObject(dir));
            }
        }
    }

    private static void addTunnels(SteppingMatrix<MineObject> matrix, Random random, List<Point> connectPositions) {
        List<Point> filtered = MyLists.filter(connectPositions, Objects::nonNull);
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
        for (int y = 0; y < matrix.getRows(); ++y) {
            for (int x = 1; x < matrix.getColumns(); ++x) {
                MineObject obj = matrix.getElementAt(x, y);
                if (obj instanceof UnbreakableRockObject) {
                    int roll = random.nextInt(6);
                    if (roll != 0) {
                        matrix.remove(obj);
                    }
                    if (roll == 1) {
                        matrix.addElement(x, y, new BreakableRockMineObject());
                    } else if (roll == 2) {
                        matrix.addElement(x, y, new MaterialsOreObject(random.nextInt(3)));
                    } else if (roll == 3) {
                        matrix.addElement(x, y, new SilverOreObject(random.nextInt(3)));
                    } else if (roll == 4) {
                        matrix.addElement(x, y, new GoldOreObject(random.nextInt(3)));
                    } else if (roll == 5) {
                        matrix.addElement(x, y, new RubyRockObject());
                    }
                }
            }
        }
    }

    public static MineRoom makeConnectingRoom(Random random, MineRoomLocation currentLocation, MineRoomMap map,
                                              MineRoom oldRoom, int direction) {
        System.out.println("From room:");
        print(oldRoom.matrix);
        List<Point> connectPositions = makeRandomConnections(random);
        int opposDir = LogicalMine.getOppositeDirection(direction);
        Set<Integer> freeConnections = new HashSet<>(List.of(0, 1, 2, 3));
        freeConnections.remove(opposDir);

        for (int dir = 0; dir < 4; ++dir) {
            if (dir == opposDir) { // Direction we came from, fix adjoining connection
                adjustConnectionToSame(connectPositions, oldRoom, opposDir, direction);
            } else { // Other connection
                System.out.println("Other room in dir: " + dir);
                MineRoomLocation otherLoc = currentLocation.copy();
                otherLoc.moveInDirection(dir);
                int otherOpposDir = LogicalMine.getOppositeDirection(dir);
                if (map.roomExists(otherLoc)) {
                    freeConnections.remove(dir);
                    MineRoom otherRoom = map.get(otherLoc);
                    if (otherRoom.getConnector(otherOpposDir) != null) { // has a door in direction
                        adjustConnectionToSame(connectPositions, otherRoom, dir, otherOpposDir);
                    } else { // No door there, remove it in this room as well
                        connectPositions.set(dir, null);
                    }
                }
            }
        }

        // freeConnections will be [0,3] in size
        System.out.println("Newly created room has " + freeConnections.size() + " free connections.");
        List<Integer> freeList = new ArrayList<>(freeConnections);
        Collections.shuffle(freeList);
        if (freeList.size() > 1 && random.nextInt(10) == 0) {
            connectPositions.set(freeList.removeFirst(), null);
        }
        if (freeList.size() > 1 && random.nextInt(10) > 1) {
            connectPositions.set(freeList.removeFirst(), null);
        }
        if (freeList.size() > 1 && random.nextInt(10) < 2) {
            connectPositions.set(freeList.removeFirst(), null);
        }

        return makeBasicRoom(random, currentLocation.level, connectPositions);
    }

    private static void adjustConnectionToSame(List<Point> connectPositions, MineRoom other, int towardOther, int towardNew) {
        Point newConnect = connectPositions.get(towardOther);
        if (towardNew == 0 || towardNew == 3) {
            newConnect.x = other.connectPositions.get(towardNew).x;
        } else {
            newConnect.y = other.connectPositions.get(towardNew).y;
        }
    }

    public void makeExit(Random random) {
        this.exitDir = random.nextInt(4);
        this.exitPos = new Point(connectPositions.get(exitDir));
        matrix.remove(matrix.getElementAt(exitPos.x, exitPos.y));
        matrix.addElement(exitPos.x, exitPos.y, new MineExitObject(exitDir));
    }

    public Point getExitPosition() {
        return exitPos;
    }

    public MyPair<MineRoom, MineRoomLocation> makeAntiRoom(Random random, int level) {
        List<Point> exits = makeRandomConnections(random);
        int opposDir = LogicalMine.getOppositeDirection(exitDir);
        exits.set(opposDir, null);
        MineRoom antiRoom = makeBasicRoom(random, level, exits);
        MineRoomLocation antiRoomLoc = new MineRoomLocation(0, 0, 1);
        antiRoomLoc.moveInDirection(exitDir);
        return new MyPair<>(antiRoom, antiRoomLoc);
    }

    public SteppingMatrix<MineObject> getMatrix() {
        return matrix;
    }

    public boolean canMoveInto(Model model, AdvancedMineEvent state, Point newPosition) {
        Rectangle r = new Rectangle(MINE_COLUMNS, MINE_ROWS);
        if (!r.contains(newPosition)) {
            return false;
        }
        MineObject obj = matrix.getElementAt(newPosition.x, newPosition.y);
        if (obj != null) {
            return obj.gotBumpedInto(model, state, newPosition);
        }
        return true;
    }

    private static MineObject makeInitialRocks(Random random) {
        if (random.nextInt(5) > 0) {
            return new UnbreakableRockObject();
        }
        return new BreakableRockMineObject();
    }

    public Point getConnector(int dir) {
        return connectPositions.get(dir);
    }
}
