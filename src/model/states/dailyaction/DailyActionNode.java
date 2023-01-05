package model.states.dailyaction;

import model.Model;
import model.states.GameState;
import view.sprites.Sprite;

import java.awt.*;
import java.io.Serializable;

public abstract class DailyActionNode implements Serializable {

    private final String name;

    public DailyActionNode(String name) {
        this.name = name;
    }

    public abstract GameState getDailyAction(Model model);


    public String getName() {
        return name;
    }

    public abstract Sprite getBackgroundSprite();

    public Sprite getForegroundSprite() {
        return null;
    }

    public void drawYourself(Model model, Point p) {
        model.getScreenHandler().put(p.x, p.y, getBackgroundSprite());
        Sprite fg = getForegroundSprite();
        if (fg != null) {
            p.x += 2;
            p.y += 2;
            model.getScreenHandler().register("objectforeground", p, fg);
        }
    }

    public Point getCursorShift() {
        return new Point(0, -2);
    }

    public abstract boolean canBeDoneDuring(Model model, TownDailyActionState townDailyActionState, int timeOfDay);

    public boolean exitsTown() {
        return false;
    }

    public abstract boolean isFreeAction();
}
