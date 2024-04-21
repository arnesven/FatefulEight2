package model.ruins.objects;

import java.awt.*;

public class PositionableRoomDecoration extends RoomDecoration {
    public PositionableRoomDecoration(int x, int y) {
        super(0, 0);
        setInternalPosition(new Point(x, y));
    }
}
