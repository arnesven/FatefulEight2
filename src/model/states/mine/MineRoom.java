package model.states.mine;

import model.Model;
import model.SteppingMatrix;
import util.MyLists;
import util.MyPair;
import util.MyRandom;

import java.awt.*;
import java.util.List;
import java.util.Random;

public class MineRoom {

    private static final int MINE_COLUMNS = 8;
    private static final int MINE_ROWS = 9;

    private static Random random = null;

    private static final java.util.List<Point> EXIT_POSITIONS = List.of(
            new Point(MINE_COLUMNS/2-1, 0),            // NORTH
            new Point(0, MINE_ROWS/2),                 // WEST
            new Point(MINE_COLUMNS-1, 4),              // EAST
            new Point(MINE_COLUMNS/2-1, MINE_ROWS-1)); // SOUTH

    private SteppingMatrix<MineObject> matrix;
    private Point exitPos = null;
    private int exitDir = -1;

    private MineRoom(SteppingMatrix<MineObject> matrix) {
        this.matrix = matrix;
    }

    public static MineRoom makeBasicRoom(Random random, int level) {
        SteppingMatrix<MineObject> matrix = new SteppingMatrix<>(MINE_COLUMNS, MINE_ROWS);
        for (int y = 0; y < matrix.getRows(); ++y) {
            for (int x = 0; x < matrix.getColumns(); ++x) {
                int finalX = x;
                int finalY = y;
                boolean isOnExit = MyLists.any(EXIT_POSITIONS, p -> p.x == finalX && p.y == finalY);
                if (!isOnExit) {
                    matrix.addElement(x, y, makeRockForLevel(level));
                }
            }
        }

        for (Point doorPos : EXIT_POSITIONS) {
            matrix.addElement(doorPos.x, doorPos.y, new MinePassageObject(doorPos));
        }

        for (int y = 0; y < matrix.getRows(); ++y) {
            for (int x = 1; x < matrix.getColumns()-1; ++x) {
                MineObject obj = matrix.getElementAt(x, y);
                MineObject left = matrix.getElementAt(x-1, y);
                MineObject right = matrix.getElementAt(x+1, y);
                if (obj == null && left != null && right != null) {
                    matrix.addElement(x, y, new MineTunnelSupportObject());
                }
            }
        }

        return new MineRoom(matrix);
    }

    public void makeExit(Random random) {
        this.exitDir = random.nextInt(4);
        this.exitPos = new Point(EXIT_POSITIONS.get(exitDir));
        matrix.remove(matrix.getElementAt(exitPos.x, exitPos.y));
        matrix.addElement(exitPos.x, exitPos.y, new MineExitObject(exitPos));
    }

    public Point getStartingPoint() {
        return exitPos;
    }

    public MyPair<MineRoom, MineRoomLocation> makeAntiRoom(Random random, int level) {
        MineRoom antiRoom = makeBasicRoom(random, level);
        int opposDir = LogicalMine.getOppositeDirection(exitDir);
        Point otherSideOfExit = EXIT_POSITIONS.get(opposDir);
        antiRoom.matrix.remove(antiRoom.matrix.getElementAt(otherSideOfExit.x, otherSideOfExit.y));
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

    public Point getPositionOppositeExit(int direction) {
        int oppDir = LogicalMine.getOppositeDirection(direction);
        return EXIT_POSITIONS.get(oppDir);
    }
}
