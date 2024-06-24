package model.states.battle;

import java.awt.*;

public enum BattleDirection {
    east(0, "east", new Point(1, 0)),
    south(1, "south", new Point(0, 1)),
    west(2, "west", new Point(-1, 0)),
    north(3, "north", new Point(0, -1));

    int value;
    String asText;
    Point dxdy;

    BattleDirection(int value, String text, Point dxdy) {
        this.value = value;
        this.asText = text;
        this.dxdy = dxdy;
    }

    public BattleDirection getOpposite() {
        switch (this) {
            case east:
                return west;
            case west:
                return east;
            case north:
                return south;
            case south:
                return north;
        }
        throw new IllegalStateException("Illegal direction!");
    }

    public boolean isOpposite(BattleDirection direction) {
        return getOpposite() == direction;
    }
}
