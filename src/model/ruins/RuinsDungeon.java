package model.ruins;

import model.Model;
import model.SteppingMatrix;
import util.MyPair;
import view.sprites.LoopingSprite;
import view.sprites.QuestCursorSprite;
import view.subviews.SubView;

import java.awt.*;
import java.io.Serializable;
import java.util.*;
import java.util.List;

public class RuinsDungeon implements Serializable {

    private final List<DungeonLevel> levels = new ArrayList<>();

    // x y
    private static final LoopingSprite cursor = new QuestCursorSprite();
    private final DungeonMap map;
    private boolean drawAvatar = true;
    private boolean drawCursor = true;
    private boolean completed = false;

    private Random random = new Random();

    public RuinsDungeon() {
        int roomsTarget = 120;
        System.out.println("Creating a dungeon...");
        int i = 0;
        for (; roomsTarget >= 16 ; ++i) {
            int levelSize;
            do {
                levelSize = 4 + random.nextInt(5);
            } while (roomsTarget < levelSize*levelSize);
            roomsTarget -= levelSize*levelSize;
            levels.add(new DungeonLevel(random, i == 0, levelSize));
            System.out.println(" Level " + i + " is " + levelSize + "x" + levelSize);
        }
        System.out.println(" Level " + i + " is the final level");
        levels.add(new FinalDungeonLevel(random));
        this.map = new DungeonMap(this);
    }

    public void drawYourself(Model model, Point currentPosition, int currentLevel, SteppingMatrix<DungeonObject> matrix) {
        drawRoomBackgrounds(model, currentPosition, currentLevel);
        drawRoomObjects(model, currentPosition, matrix);
        if (drawAvatar) {
            drawAvatar(model, currentPosition, currentLevel);
        }
    }

    public List<MyPair<DungeonObject, Point>> getObjectsAndPositions(Point viewPoint, int currentLevel) {
        int minX = (viewPoint.x / 2) * 2;
        int minY = (viewPoint.y / 2) * 2;
        List<MyPair<DungeonObject, Point>> result = new ArrayList<>();
        int x = viewPoint.x;
        int y = viewPoint.y;
        DungeonRoom[][] rooms = getLevel(currentLevel).getRooms();
        for (DungeonObject dObj: rooms[x][y].getObjects()) {
            int xPos = dObj.getInternalPosition().x + (x - minX)*3;
            int yPos = dObj.getInternalPosition().y + (y - minY)*3;
            result.add(new MyPair<>(dObj, new Point(xPos, yPos)));
        }
        return result;
    }

    private void drawRoomBackgrounds(Model model, Point currentPosition, int currentLevel) {
        int minX = (currentPosition.x / 2) * 2;
        int minY = (currentPosition.y / 2) * 2;
        DungeonRoom[][] rooms = getLevel(currentLevel).getRooms();
        for (int y = minY; y < minY+2 && y < rooms.length; ++y) {
            for (int x = minX; x < minX+2 && x < rooms.length; ++x) {
                if (rooms[x][y] != null) {
                    boolean leftCorner = false;
                    boolean rightCorner = false;
                    if (y > 0) {
                        leftCorner = connectsLeft(rooms, x, y-1);
                        rightCorner = connectsRight(rooms, x, y-1);
                    }
                    Point pos = convertToScreen(new Point(3*(x - minX), 3*(y - minY)));
                    rooms[x][y].drawYourself(model, pos, connectsLeft(rooms, x, y), connectsRight(rooms, x, y), leftCorner, rightCorner);

                    if (!(currentPosition.x == x && currentPosition.y == y)) {
                        for (DungeonObject dObj : rooms[x][y].getObjects()) {
                            dObj.drawYourself(model,
                                    pos.x + dObj.getInternalPosition().x*4,
                                    pos.y + dObj.getInternalPosition().y*4);
                        }
                    }

                }
            }
        }
    }

    private void drawRoomObjects(Model model, Point currentPosition, SteppingMatrix<DungeonObject> matrix) {
        for (int row = 0; row < matrix.getRows(); ++row) {
            for (int col = 0; col < matrix.getColumns(); ++col) {
                if (matrix.getElementAt(col, row) != null) {
                    Point conv = convertToScreen(new Point(col, row));
                    int xPos = conv.x;
                    int yPos = conv.y;
                    matrix.getElementAt(col, row).drawYourself(model, xPos, yPos);

                    if (matrix.getSelectedElement() == matrix.getElementAt(col, row) && drawCursor) {
                        model.getScreenHandler().register("questcursor", new Point(xPos, yPos),
                                cursor, 4);
                    }
                }
            }
        }
    }

    private void drawAvatar(Model model, Point currentPosition, int currentLevel) {
        Point p = avatarPosition(currentPosition);
        Point relativeAvatarPosition = getLevel(currentLevel).getRoom(currentPosition).getRelativeAvatarPosition();
        p.x += relativeAvatarPosition.x * 4;
        p.y += relativeAvatarPosition.y * 4;
        model.getScreenHandler().register("partyavatar", p,
                model.getParty().getLeader().getAvatarSprite(), 2);
    }

    public static Point avatarPosition(Point currentPosition) {
        int x = (currentPosition.x % 2) * 3 + 1;
        int y = (currentPosition.y % 2) * 3 + 1;
        return convertToScreen(new Point(x, y));
    }

    private static Point convertToScreen(Point point) {
        int xStart = SubView.X_OFFSET + 4 * point.x + 2;
        int yStart = SubView.Y_OFFSET + 4 * point.y + 4;
        return new Point(xStart, yStart);
    }

    private boolean connectsRight(DungeonRoom[][] rooms, int x, int y) {
        if (x == rooms.length - 1) {
            return false;
        }
        return rooms[x+1][y] != null;
    }

    private boolean connectsLeft(DungeonRoom[][] rooms, int x, int y) {
        if (x == 0) {
            return false;
        }
        return rooms[x-1][y] != null;
    }

    public void setAvatarEnabled(boolean b) {
        this.drawAvatar = b;
    }

    public void setCursorEnabled(boolean b) {
        this.drawCursor = b;
    }

    public DungeonLevel getLevel(int index) {
        return levels.get(index);
    }

    public DungeonMap getMap() {
        return map;
    }

    public int getNumberOfLevels() {
        return levels.size();
    }

    public void setCompleted(boolean b) {
        completed = b;
    }

    public boolean isCompleted() {
        return completed;
    }
}
