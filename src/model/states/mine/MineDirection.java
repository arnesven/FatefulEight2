package model.states.mine;

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
}
