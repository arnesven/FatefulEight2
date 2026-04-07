package model.states.mine;

import java.awt.*;

public enum MineDirection {
    north, west, east, south, up, down;

    public MineDirection getOpposite() {
        return switch (this) {
            case up -> down;
            case down -> up;
            case north -> south;
            case south -> north;
            case east -> west;
            case west -> east;
        };
    }

    public Point getDxDy() {
        return switch (this) {
            case north -> new Point(0, -1);
            case south -> new Point(0, 1);
            case west -> new Point(-1, 0);
            case east -> new Point(1, 0);
            default -> new Point(0, 0);
        };
    }
}
