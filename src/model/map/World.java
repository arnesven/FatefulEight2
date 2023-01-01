package model.map;

import model.Model;
import view.DrawingArea;
import view.ScreenHandler;
import view.sprites.Sprite;
import view.subviews.MapSubView;
import view.subviews.SubView;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class World implements Serializable {

    //  x   y
    private WorldHex[][] hexes;
    private ViewPointMarker cursor = new HexCursorMarker();
    private boolean avatarEnabled = true;

    public World() {
        hexes = WorldBuilder.buildWorld();
    }

    public static Point translateToScreen(Point logicPosition, Point viewPoint, int mapXRange, int mapYRange) {
        Interval xVals = calcXValues(viewPoint.x, mapXRange);
        Interval yVals = calcYValues(viewPoint.y, mapYRange);
        int startX = (DrawingArea.WINDOW_COLUMNS - mapXRange*4)/2;

        int x = logicPosition.x;
        int y = logicPosition.y;
        int yOffset = SubView.Y_OFFSET;

        int col = x - xVals.from;
        int row = y - yVals.from;

        int screenX = startX + 4*col;
        int y_extra = 2 * (1 - (x % 2));
        int screenY = yOffset - 2 + 4 * row + y_extra;

        return new Point(screenX, screenY);
    }

    public boolean crossesRiver(Point position, String directionName) {
        WorldHex hex = getHex(position);
        return hex.getRiversInDirection(directionName);
    }


    public void drawYourself(Model model, Point viewPoint, Point partyPosition,
                             int mapXRange, int mapYRange, int yOffset, Point cursorPos,
                             boolean avatarEnabled) {
        ScreenHandler screenHandler = model.getScreenHandler();
        Interval xVals = calcXValues(viewPoint.x, mapXRange);
        Interval yVals = calcYValues(viewPoint.y, mapYRange);
        int startX = (DrawingArea.WINDOW_COLUMNS - mapXRange*4)/2;
        screenHandler.clearSpace(startX, (DrawingArea.WINDOW_COLUMNS - startX),
                yOffset, yOffset + mapYRange*4 - 2);

        int row = 0;
        for (int y = yVals.from; y <= yVals.to; ++y) {
            int col = 0;
            for (int x = xVals.from; x <= xVals.to; ++x) {
                int screenX = startX + 4*col;
                int y_extra = 2 * (1 - (x % 2));
                int screenY = yOffset - 2 + 4 * row + y_extra;

                if (hexes[x][y] != null) {
                    if (screenY == yOffset - 2) {
                        hexes[x][y].drawLowerHalf(screenHandler, screenX, screenY);
                    } else if (screenY == yOffset - 2 + 4 * (mapYRange-1) + 2) {
                        hexes[x][y].drawUpperHalf(screenHandler, screenX, screenY);
                    } else {
                        hexes[x][y].drawYourself(screenHandler, screenX, screenY);
                    }
                }

                if (x == partyPosition.x && y == partyPosition.y &&
                        model.getParty().getLeader() != null && avatarEnabled) {
                    Sprite avatar = model.getParty().getLeader().getAvatarSprite();
                    screenHandler.register(avatar.getName(), new Point(screenX, screenY), avatar, 1);
                }
                if ((cursorPos == null && (x == viewPoint.x && y == viewPoint.y)) ||
                    (cursorPos != null && (x == cursorPos.x && y == cursorPos.y))) {
                        cursor.updateYourself(screenHandler, screenX, screenY);
                }
                col++;
            }
            row++;
        }
    }

    private static Interval calcXValues(int x, int mapXRange) {
        int xMin = x - mapXRange/2;
        int xMax = x + mapXRange/2 - 1;
        if (xMin < 0) {
            xMin = 0;
            xMax = mapXRange - 1;
        } else if (xMax >= WorldBuilder.WORLD_WIDTH) {
            xMax = WorldBuilder.WORLD_WIDTH - 1;
            xMin = WorldBuilder.WORLD_WIDTH - mapXRange;
        }
        return new Interval(xMin, xMax);
    }

    private static Interval calcYValues(int y, int mapYRange) {
        int yMin = y - mapYRange/2;
        int yMax = y + mapYRange/2 - 1;
        if (yMin < 0) {
            yMin = 0;
            yMax = mapYRange - 1;
        } else if (yMax >= WorldBuilder.WORLD_HEIGHT) {
            yMax = WorldBuilder.WORLD_HEIGHT - 1;
            yMin = WorldBuilder.WORLD_HEIGHT - mapYRange;
        }
        return new Interval(yMin, yMax);
    }

    public WorldHex getHex(Point position) {
        return hexes[position.x][position.y];
    }


    public static void move(Point position, int dx, int dy) {
        if (position.x == 0 && dx < 0) {
            dx = 0;
        }
        if (position.x == WorldBuilder.WORLD_WIDTH - 1 && dx > 0) {
            dx = 0;
        }
        if (position.y == 0 && dy < 0) {
            dy = 0;
        }
        if (position.y == WorldBuilder.WORLD_HEIGHT - 1 && dy > 0) {
            dy = 0;
        }
        position.x += dx;
        position.y += dy;
        if (position.y == 0 && position.x % 2 == 1) {
            position.y = 1;
        }
        if (position.y == WorldBuilder.WORLD_HEIGHT - 1 && position.x % 2 == 0) {
            position.y = WorldBuilder.WORLD_HEIGHT - 2;
        }
    }

    public List<LordLocation> getLordLocations() {
        List<LordLocation> result = new ArrayList<LordLocation>();
        for (int y = 0; y < hexes[0].length; ++y) {
            for (int x = 0; x < hexes.length; ++x) {
                if (hexes[x][y].hasLord()) {
                    result.add((LordLocation)(hexes[x][y].getLocation()));
                }
            }
        }
        return result;
    }

    public boolean canTravelTo(Model model, Point p) {
        if (p.x < 0 || p.x >= WorldBuilder.WORLD_WIDTH) {
            return false;
        }
        if (p.y < 0 || p.y >= WorldBuilder.WORLD_HEIGHT) {
            return false;
        }
        return getHex(p).canTravelTo(model);
    }

    private static class Interval {
        public int from;
        public int to;

        public Interval(int xMin, int xMax) {
            from = xMin;
            to = xMax;
        }
    }
}
