package model.map.objects;

import model.Model;
import model.actions.DailyAction;
import view.ScreenHandler;
import view.help.HelpDialog;

import java.awt.*;
import java.util.List;
import java.io.Serializable;

public abstract class MapObject implements Serializable {
    private final Point position;
    private final String description;

    public MapObject(Point position, String description) {
        this.position = position;
        this.description = description;
    }

    public Point getPosition() {
        return position;
    }

    public String getDescription() {
        return description;
    }

    public abstract List<DailyAction> getDailyActions(Model model);

    public abstract void drawLowerHalf(ScreenHandler screenHandler, int x, int y);

    public abstract void drawUpperHalf(ScreenHandler screenHandler, int x, int y);

    public void drawYourself(ScreenHandler screenHandler, int x, int y) {
        drawUpperHalf(screenHandler, x, y);
        drawLowerHalf(screenHandler, x, y+2);
    }

    public abstract HelpDialog getHelpDialog(Model model);
}
