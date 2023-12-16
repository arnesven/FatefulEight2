package model.ruins;

import model.Model;
import model.states.ExploreRuinsState;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class OpenDoor extends DungeonDoor {


    private final String direction;
    private final boolean isHorizontal;
    private List<LeverObject> levers = new ArrayList<>();

    public OpenDoor(Point point, boolean isHorizontal, String direction) {
        super(point.x, point.y);
        this.isHorizontal = isHorizontal;
        this.direction = direction;
    }

    @Override
    protected Sprite getSprite(DungeonTheme theme) {
        return theme.getDoor(isHorizontal, false);
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos, DungeonTheme theme) {
        super.drawYourself(model, xPos, yPos, theme);
        if (isHorizontal) {
            Sprite spr = theme.getDoorOverlay();
            model.getScreenHandler().register(spr.getName(), new Point(xPos, yPos), spr, 3);
        }
        if (!levers.isEmpty() && leversWrong()) {
            Sprite spr = theme.getDoor(isHorizontal, true);
            model.getScreenHandler().register(spr.getName(), new Point(xPos, yPos), spr, 1);
        }
    }

    private boolean leversWrong() {
        for (LeverObject lever : levers) {
            if (!lever.isOn()) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getDescription() {
        return "A door going " + this.direction;
    }

    @Override
    public void doAction(Model model, ExploreRuinsState state) {
        if (!levers.isEmpty() && leversWrong()) {
            state.println("This door's lock cannot be picked.");
        } else {
            if (direction.equals("north")) {
                state.goDirection(model, new Point(0, -1));
            } else if (direction.equals("south")) {
                state.goDirection(model, new Point(0, 1));
            } else if (direction.equals("east")) {
                state.goDirection(model, new Point(1, 0));
            } else {
                state.goDirection(model, new Point(-1, 0));
            }
        }
    }

    public void addLeverConnection(LeverObject lever) {
        this.levers.add(lever);
    }
}
