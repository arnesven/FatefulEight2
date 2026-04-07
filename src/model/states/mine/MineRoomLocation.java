package model.states.mine;

import java.awt.*;

public class MineRoomLocation {
    Point xy;
    int level;

    public MineRoomLocation(int x, int y, int level) {
        xy = new Point(x, y);
        this.level = level;
    }

    public String asString() {
        return xy.x + ":" + xy.y + ":" + level;
    }

    public void moveInDirection(MineDirection direction) {
        if (direction == MineDirection.north) {
            xy.y--;
        } else if (direction == MineDirection.south) {
            xy.y++;
        } else if (direction == MineDirection.west) {
            xy.x--;
        } else if (direction == MineDirection.east) {
            xy.x++;
        } else if (direction == MineDirection.up) {
            level--;
        } else { // Down
            level++;
        }
    }

    public MineRoomLocation copy() {
        return new MineRoomLocation(xy.x, xy.y, level);
    }
}
