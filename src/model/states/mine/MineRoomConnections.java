package model.states.mine;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MineRoomConnections {

    private HashMap<MineDirection, Point> map = new HashMap<>();

    public Point put(MineDirection key, Point value) {
        if (value.x < 0 || value.y < 0) {
            throw new IllegalArgumentException("Negative position for connection");
        }
        return map.put(key, value);
    }

    public Point get(MineDirection key) {
        return map.get(key);
    }

    public void remove(MineDirection mineDirection) {
        map.remove(mineDirection);
    }

    public List<Point> getAsList() {
        return new ArrayList<>(map.values());
    }
}
