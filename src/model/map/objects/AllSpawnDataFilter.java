package model.map.objects;

import model.Model;
import model.journal.*;
import model.map.WorldBuilder;
import model.map.WorldHex;
import util.MyPair;
import view.BorderFrame;
import view.DrawingArea;
import view.MyColors;
import view.ScreenHandler;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AllSpawnDataFilter extends MapFilter {

    private static final int WIDTH = 13;
    private static final int HEIGHT = 6;
    private static final MyColors BACKGROUND_COLOR = MyColors.BLACK;

    private static final MyColors[] colors = {MyColors.BLUE, MyColors.RED, MyColors.DARK_PURPLE, MyColors.GOLD};
    private static final Map<MyColors, Sprite[]> borderSprites = makeOverlaySprites();

    private final List<MainStorySpawnLocation> spawnLocations = List.of(
            new MainStorySpawnNorth(), new MainStorySpawnEast(),
            new MainStorySpawnWest(), new MainStorySpawnSouth());

    @Override
    public List<MyPair<Point, Sprite>> getObjects(Model model) {
        List<MyPair<Point, Sprite>> list = new ArrayList<>();
        int i = 0;
        for (MainStorySpawnLocation spawn : spawnLocations) {
            list.addAll(makeBorderSprites(spawn, colors[i]));
            ++i;
        }
        return list;
    }

    private List<MyPair<Point, Sprite>> makeBorderSprites(MainStorySpawnLocation spawn, MyColors color) {
        List<MyPair<Point, Sprite>> list = new ArrayList<>();
        Point ul = spawn.getPastUpperLeftCornerPoint();
        int xMax = ul.x + WorldBuilder.OTHER_BOUNDS.width - 1;
        int yMax = ul.y + WorldBuilder.OTHER_BOUNDS.height - 1;
        for (int y = ul.y; y <= yMax; ++y) {
            for (int x = ul.x; x <= xMax; ++x) {
                if (x == ul.x || x == xMax) {
                    list.add(new MyPair<>(new Point(x, y), borderSprites.get(color)[1]));
                    list.add(new MyPair<>(new Point(x, y), borderSprites.get(color)[4]));
                }
                if (y == ul.y || y == yMax) {
                    if (x % 2 == 0) {
                        list.add(new MyPair<>(new Point(x, y), borderSprites.get(color)[3]));
                        list.add(new MyPair<>(new Point(x, y), borderSprites.get(color)[5]));
                    } else {
                        list.add(new MyPair<>(new Point(x, y), borderSprites.get(color)[0]));
                        list.add(new MyPair<>(new Point(x, y), borderSprites.get(color)[2]));
                    }
                }
            }
        }
        return list;
    }

    @Override
    public void drawLegend(ScreenHandler screenHandler, int x, int y) {
        screenHandler.clearForeground(x, x+WIDTH, y-1, y+HEIGHT-1);
        BorderFrame.drawFrame(screenHandler, x, y-1, WIDTH, HEIGHT,
                MyColors.BLACK, MyColors.GRAY, BACKGROUND_COLOR, true);

        BorderFrame.drawString(screenHandler, "  SPAWN DATA  ", x+1, y, MyColors.WHITE, BACKGROUND_COLOR);
        BorderFrame.drawString(screenHandler, (char)(0xFF) + " North", x+1, y+1, colors[0], BACKGROUND_COLOR);
        BorderFrame.drawString(screenHandler, (char)(0xFF) + " East", x+1, y+2, colors[1], BACKGROUND_COLOR);
        BorderFrame.drawString(screenHandler, (char)(0xFF) + " West", x+1, y+3, colors[2], BACKGROUND_COLOR);
        BorderFrame.drawString(screenHandler, (char)(0xFF) + " South", x+1, y+4, colors[3], BACKGROUND_COLOR);
    }

    @Override
    public void drawSpecial(Model model, WorldHex[][] hexes, int x, int y, int screenX, int screenY) {
        int i = 0;
        for (MainStorySpawnLocation spawn : spawnLocations) {
            drawEntryPoint(model, spawn, x, y, screenX, screenY, colors[i]);
            ++i;
        }
    }

    private void drawEntryPoint(Model model, MainStorySpawnLocation spawn, int x, int y, int screenX, int screenY, MyColors textColor) {
        Point ulCorner = spawn.getPastUpperLeftCornerPoint();
        Point past = spawn.getPastEntryPoint();
        Point p = new Point(ulCorner.x + past.x,
                            ulCorner.y + past.y);
        drawAt(model, p, x, y, screenX, screenY, textColor, "PAST ENTRY");
    }

    private void drawAt(Model model, Point point, int x, int y, int screenX, int screenY, MyColors textColor, String text) {
        if (point.x == x && point.y == y) {
            String name = text;
            String[] parts = name.split(" ");
            for (int i = 0; i < parts.length; ++i) {

                int finalX = Math.min(DrawingArea.WINDOW_COLUMNS - parts[i].length(),
                        Math.max(0, screenX + 2 - parts[i].length() / 2));

                BorderFrame.drawStringInForeground(model.getScreenHandler(), parts[i],
                        finalX, 2 + screenY + i, textColor, MyColors.BLACK);
            }
        }
    }

    private static Map<MyColors, Sprite[]> makeOverlaySprites() {
        Map<MyColors, Sprite[]> result = new HashMap<>();
        for (MyColors col : colors) {
            Sprite[] sprites = new Sprite[6];
            for (int i = 0; i < 6; ++i) {
                sprites[i] = new Sprite32x32("border", "world.png", 0x61 + i,
                                col, MyColors.GRAY, MyColors.BEIGE);
            }
            result.put(col, sprites);
        }
        return result;
    }
}
