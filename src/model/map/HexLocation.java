package model.map;

import model.Model;
import model.actions.DailyAction;
import view.subviews.SubView;
import view.subviews.ImageSubView;
import view.ScreenHandler;
import view.sprites.Sprite;

import java.awt.*;
import java.util.List;

public abstract class HexLocation {
    private final String name;

    public HexLocation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void drawUpperHalf(ScreenHandler screenHandler, int x, int y) {
        screenHandler.register(name + "uppersprite"+x+""+y, new Point(x, y), getUpperSprite());
    }

    public void drawLowerHalf(ScreenHandler screenHandler, int x, int y) {
        screenHandler.register(name + "lowersprite"+x+""+y, new Point(x, y+2), getLowerSprite());
    }

    protected abstract Sprite getLowerSprite();

    protected abstract Sprite getUpperSprite();

    public abstract boolean isDecoration();

    public boolean hasLodging() { return false; }

    public SubView getImageSubView() { return null; }

    public boolean hasDailyActions() {
        return false;
    }

    public List<DailyAction> getDailyActions(Model model) {
        return null;
    }

    public void travelTo(Model model) { }

    public void travelFrom(Model model) { }

    public boolean givesQuests() {
        return false;
    }
}
