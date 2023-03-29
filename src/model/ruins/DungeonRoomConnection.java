package model.ruins;

import java.awt.*;

public enum DungeonRoomConnection {
    NONE,
    OPEN,
    LOCKED,
    STAIRS_UP,
    STAIRS_DOWN;

    public static DungeonDoor makeObject(DungeonRoomConnection door, Point point, boolean isHorizontal, String direction) {
        switch (door) {
            case OPEN:
                return new OpenDoor(point, isHorizontal, direction);
            case LOCKED:
                return new LockedDoor(point, isHorizontal, direction);
            case STAIRS_UP:
                return new StairsUp(point);
            case STAIRS_DOWN:
                return new StairsDown(point);
        }
        throw new IllegalStateException("Cannot make object for door type " + door);
    }
}
