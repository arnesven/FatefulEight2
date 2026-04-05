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

    public void moveInDirection(int direction) {
        if (direction == 0) {
            xy.y--;
        } else if (direction == 3) {
            xy.y++;
        } else if (direction == 1) {
            xy.x--;
        } else {
            xy.x++;
        }
    }
}
