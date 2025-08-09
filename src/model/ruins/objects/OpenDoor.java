package model.ruins.objects;

import model.Model;
import model.ruins.themes.DungeonTheme;
import model.states.ExploreRuinsState;
import view.sprites.Sprite;
import view.subviews.DungeonDrawer;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class OpenDoor extends DungeonDoor {


    private final String direction;
    private final boolean isHorizontal;
    private List<LeverObject> levers = new ArrayList<>();
    private boolean blocked;

    public OpenDoor(Point point, boolean isHorizontal, String direction) {
        super(point.x, point.y);
        this.isHorizontal = isHorizontal;
        this.direction = direction;
        blocked = false;
    }

    public boolean isHorizontal() {
        return isHorizontal;
    }

    @Override
    protected Sprite getSprite(DungeonTheme theme) {
        return theme.getDoor(isHorizontal, false);
    }

    @Override
    public void drawYourself(DungeonDrawer drawer, int xPos, int yPos, DungeonTheme theme) {
        super.drawYourself(drawer, xPos, yPos, theme);
        if (isHorizontal) {
            Sprite spr = theme.getDoorOverlay();
            drawer.register(spr.getName(), new Point(xPos, yPos), spr, 3);
        }
        if (!levers.isEmpty() && leversWrong()) {
            Sprite spr = theme.getDoor(isHorizontal, true);
            drawer.register(spr.getName(), new Point(xPos, yPos), spr, 1);
        }
    }

    public boolean leversWrong() {
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
        if (blocked) {
            state.println("You cannot use this door at the moment (you may have to move closer).");
            return;
        }
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
        lever.setDoor(this);
    }

    public void setBlocked(boolean blocked) {
        this.blocked = blocked;
    }
}
