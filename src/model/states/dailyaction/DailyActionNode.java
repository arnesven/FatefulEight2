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

    public abstract GameState getDailyAction(Model model, AdvancedDailyActionState state);

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
            model.getScreenHandler().register("objectforeground", p, fg);
        }
    }

    public Point getCursorShift() {
        return new Point(0, -2);
    }

    public abstract boolean canBeDoneRightNow(AdvancedDailyActionState state, Model model);

    public boolean exitsCurrentLocale() {
        return false;
    }

    public abstract void setTimeOfDay(Model model, AdvancedDailyActionState state);

    public boolean returnNextState() {
        return false;
    }
}
