package view.subviews;

import view.ScreenHandler;
import view.sprites.Sprite;

import java.awt.*;

public class DungeonDrawer {

    private static DungeonDrawer instance;
    private final ScreenHandler handler;
    private final Rectangle bounds;

    public DungeonDrawer(ScreenHandler handler) {
        this.handler = handler;
        this.bounds =  new Rectangle(SubView.X_OFFSET, SubView.Y_OFFSET + 4,
                SubView.X_MAX-SubView.X_OFFSET-4, SubView.Y_MAX - (SubView.Y_OFFSET + 4) - 8);
    }

    public static DungeonDrawer getInstance(ScreenHandler screenHandler) {
        if (instance == null) {
            instance = new DungeonDrawer(screenHandler);
        }
        return instance;
    }

    public void put(int x, int y, Sprite sprite) {
        if (bounds.contains(x, y)) {
            handler.put(x, y, sprite);
        }
    }

    public void register(String s, Point p, Sprite sprite, int priority) {
        if (bounds.contains(p)) {
            handler.register(s, p, sprite, priority);
        }
    }

    public void register(String s, Point p, Sprite sprite) {
        register(s, p, sprite, 0);
    }

    public void putNoRestriction(int xPos, int yPos, Sprite sprite) {
        handler.put(xPos, yPos, sprite);
    }

    public void registerNoRestriction(String name, Point point, Sprite sprite) {
        handler.register(name, point, sprite);
    }
}
