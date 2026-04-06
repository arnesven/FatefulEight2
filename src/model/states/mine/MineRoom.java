package model.states.mine;

import model.Model;
import model.SteppingMatrix;
import util.MyLists;
import util.MyPair;
import util.MyRandom;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

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
        addSupports(matrix);
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
                    matrix.addElement(x, y, makeRockForLevel(level));
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
        for (int i = 0; i < filtered.size() - 1; ++i) {
            addTunnel(matrix, random, new Point(filtered.get(i)), new Point(filtered.get(i+1)));
        }
    }

    private static void addTunnel(SteppingMatrix<MineObject> matrix, Random random, Point start, Point end) {
        System.out.println("Making tunnel from " + start + " to " + end);
        int horiSteps = end.x - start.x;
        int vertiSteps = end.y - start.y;

        do {
            System.out.println("Hori steps " + horiSteps + ", verti steps: " + vertiSteps);
            print(matrix);
            boolean step;
            if (horiSteps != 0 && vertiSteps != 0) {
                step = random.nextBoolean();
            } else {
                step = horiSteps != 0;
            }

            if (step) {
                System.out.println("Step x");
                int dx = (int) Math.signum(horiSteps);
                start.x += dx;
                horiSteps -= dx;
            } else {
                System.out.println("Step y");
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
                } else if (obj instanceof RockMineObject) {
                    System.out.print("R");
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
        return obj instanceof RockMineObject;
    }

    public static MineRoom makeConnectingRoom(Random random, int level, MineRoom currentRoom, int direction) {
        List<Point> connectPositions = makeRandomConnections(random);
        int opposDir = LogicalMine.getOppositeDirection(direction);
        Point newExit = connectPositions.get(opposDir);
        if (direction == 0 || direction == 3) {
            newExit.x = currentRoom.connectPositions.get(direction).x;
        } else {
            newExit.y = currentRoom.connectPositions.get(direction).y;
        }
        return makeBasicRoom(random, level, connectPositions);
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

    private static MineObject makeRockForLevel(int level) {
        if (MyRandom.flipCoin()) {
            return new UnbreakableRockObject();
        }
        return new BreakableRockMineObject();
    }

    public Point getConnector(int dir) {
        return connectPositions.get(dir);
    }
}
