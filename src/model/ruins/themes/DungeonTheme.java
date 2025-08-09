package model.ruins.themes;

import model.ruins.DungeonRoom;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.io.Serializable;

public interface DungeonTheme extends Serializable {
    Sprite[] getConnect(); // 3 elements {T-shaped wall, vertical wall with floor on both sides, horizontal wall}
    Sprite[] getLeft(); // 4 elements {Upper left corner wall, vertical wall with floor on right side, lower left corner wall, possibly don't care?}
    Sprite[] getMid(); // 4 elements {Horizontal wall, floor, possibly unused, dark (used for corridors) }
    Sprite[] getRight(); // 4 elements {Upper right corner, vertical wall with floor on left side, lower right corner wall, possibly don't care?}

    Sprite getStairsDown();
    Sprite getStairsUp();

    Sprite getDoor(boolean isHorizontal, boolean isLeverDoor);
    Sprite getDoorOverlay();

    Sprite getCrackedWall(boolean isHorizontal);
    Sprite32x32 getLockedDoor(boolean isHorizontal);

    Sprite getLever(boolean on);

    default void setRoom(DungeonRoom dungeonRoom) {}
}
