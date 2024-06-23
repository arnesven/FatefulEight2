package model.states.battle;

import model.Model;
import model.SteppingMatrix;
import view.ScreenHandler;
import view.sprites.Sprite;

import java.awt.*;
import java.io.Serializable;

public abstract class BattleUnit implements Serializable {

    public enum Direction {
        east(0, "east",   new Point(1, 0)),
        south(1, "south", new Point(0, 1)),
        west(2, "west",   new Point(-1, 0)),
        north(3, "north", new Point(0, -1));

        int value;
        String asText;
        Point dxdy;

        Direction(int value, String text, Point dxdy) {
            this.value = value;
            this.asText = text;
            this.dxdy = dxdy;
        }

        public boolean isOpposite(Direction direction) {
            switch (this) {
                case east:
                    return direction == west;
                case west:
                    return direction == east;
                case north:
                    return direction == south;
                case south:
                    return direction == north;
            }
            return false;
        }
    }

    private final String name;
    private final int count;
    private final String origin;
    private Direction direction = Direction.east;

    public BattleUnit(String name, int count, String origin) {
        this.name = name;
        this.count = count;
        this.origin = origin;
    }

    public abstract BattleUnit copy();

    protected abstract Sprite[] getSprites();

    public void drawYourself(ScreenHandler screenHandler, Point p, int prio) {
        Sprite spr = getSprites()[direction.value];
        screenHandler.register(spr.getName(), p, spr, prio);
    }

    public String getName() {
        return name;
    }

    public int getCount() {
        return count;
    }

    public String getOrigin() {
        return origin;
    }

    public void setDirection(Direction dir) {
        this.direction = dir;
    }

    public Direction getDirection() {
        return direction;
    }
}
