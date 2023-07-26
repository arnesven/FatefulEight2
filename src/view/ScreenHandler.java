package view;

import view.sprites.Animation;
import view.sprites.FilledBlockSprite;
import view.sprites.Sprite;

import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class ScreenHandler {
    private final int state;
    private final Map<Point, Sprite> backgroundSprites = new HashMap<>();
    private final List<ForegroundObject> foregroundSprites = new ArrayList<>();
    private boolean enableForeground = true;
    private double fadeLevel;
    private MyColors fadeColor;
    private int oldNumberOfFgSprites = 0;

    public ScreenHandler() {
        this.state = 0;
    }

    public synchronized void drawBackground(Graphics g) {
        List<Map.Entry<Point, Sprite>> toDraw = new ArrayList<>();
        toDraw.addAll(backgroundSprites.entrySet());
        Collections.sort(toDraw, (e1, e2) -> e2.getValue().getWidth() - e1.getValue().getWidth());
        for (Map.Entry<Point, Sprite> entry : toDraw) {
            draw(g, entry.getKey(), entry.getValue(), 0, 0);
        }
    }

    public synchronized void drawForeground(Graphics g, int xOffset, int yOffset) {
        if (foregroundSprites.size() > 10000) {
            throw new IllegalStateException("Very many foreground objects! Size = " + foregroundSprites.size());
        }
        Collections.sort(foregroundSprites);
        for (ForegroundObject obj : foregroundSprites) {
            draw(g, obj.position, obj.sprite, xOffset + obj.xShift, yOffset + obj.yShift);
        }
    }

    private static void draw(Graphics g, Point key, Sprite sprite, int xOffset, int yOffset) {
        try {
            int x = (int) (key.getX() * 8) + xOffset;
            int y = (int) (key.getY() * 8) + yOffset;
            g.drawImage(sprite.getImage(), x, y, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public synchronized void put(int col, int row, Sprite sprite) {
        if (sprite == null) {
            throw new IllegalArgumentException("Tried adding null sprite to background!");
        }
        backgroundSprites.put(new Point(col, row), sprite);
    }

    public synchronized void clear(int x, int y) {
        backgroundSprites.remove(new Point(x,y));
    }

    public synchronized void register(String s, Point p, Sprite spr, int prio, int xShift, int yShift) {
        foregroundSprites.add(new ForegroundObject(s, p, spr, prio, xShift, yShift));
    }

    public synchronized void register(String s, Point p, Sprite spr, int prio) {
        foregroundSprites.add(new ForegroundObject(s, p, spr, prio, 0, 0));
    }


    public synchronized void register(String s, Point p, Sprite spr) {
        foregroundSprites.add(new ForegroundObject(s, p, spr, 0, 0, 0));
    }

    public synchronized void clearSpace(int xStart, int xEnd, int yStart, int yEnd) {
        for (int y = yStart; y < yEnd; ++y) {
            for (int x = xStart; x < xEnd; ++x) {
                clear(x, y);
                //put(x, y, new CharSprite('X', MyColors.WHITE));
            }
        }
    }

    public synchronized void clearForeground() {
        foregroundSprites.clear();
    }

    public synchronized void clearAll() {
        backgroundSprites.clear();
        foregroundSprites.clear();
        setFade(0.0, MyColors.BLACK);
    }


    public synchronized void fillSpace(int xStart, int xEnd, int yStart, int yEnd, Sprite sprite) {
        for (int y = yStart; y < yEnd; ++y) {
            for (int x = xStart; x < xEnd; ++x) {
                put(x, y, sprite);
            }
        }
    }

    public synchronized void fillForeground(int xStart, int xEnd, int yStart, int yEnd, Sprite sprite, int prio) {
        for (int y = yStart; y < yEnd; ++y) {
            for (int x = xStart; x < xEnd; ++x) {
                register(sprite.getName()+""+x+""+y, new Point(x, y), sprite, prio);
            }
        }
    }

    public synchronized void clearForeground(int xStart, int xEnd, int yStart, int yEnd) {
        Set<ForegroundObject> toRemove = new HashSet<>();
        for (ForegroundObject obj : foregroundSprites) {
            int x = obj.position.x;
            int y = obj.position.y;
            if (xStart <= x && x <= xEnd) {
                if (yStart <= y && y <= yEnd) {
                    toRemove.add(obj);
                }
            }
        }
        foregroundSprites.removeAll(toRemove);
    }

    public boolean foregroundEnabled() {
        return enableForeground;
    }

    public void setForegroundEnabled(boolean b) {
        enableForeground = b;
    }

    public void setFade(double fadeLevel, MyColors black) {
        this.fadeLevel = fadeLevel;
        this.fadeColor = black;
    }

    public double getFadeLevel() {
        return fadeLevel;
    }

    public MyColors getFadeColor() {
        return fadeColor;
    }

    public void unregister(String key) {
        foregroundSprites.removeIf((ForegroundObject fo) -> fo.key.equals(key));
    }

    private class ForegroundObject implements Comparable<ForegroundObject> {
        private final int xShift;
        private final int yShift;
        public String key;
        public Point position;
        public Sprite sprite;
        public int priority;

        public ForegroundObject(String s, Point p, Sprite spr, int i, int x, int y) {
            key = s;
            position = p;
            sprite = spr;
            priority = i;
            this.xShift = x;
            this.yShift = y;
        }

        @Override
        public int compareTo(ForegroundObject foregroundObject) {
            return priority - foregroundObject.priority;
        }
    }
}
