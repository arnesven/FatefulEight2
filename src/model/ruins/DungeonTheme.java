package model.ruins;

import view.sprites.Sprite;
import view.sprites.Sprite32x32;

import java.io.Serializable;

public interface DungeonTheme extends Serializable {
    Sprite[] getConnect();
    Sprite[] getLeft();
    Sprite[] getMid();
    Sprite[] getRight();

    Sprite getStairsDown();
    Sprite getStairsUp();

    Sprite getDoor(boolean isHorizontal, boolean isLeverDoor);
    Sprite getDoorOverlay();

    Sprite getCrackedWall(boolean isHorizontal);
    Sprite32x32 getLockedDoor(boolean isHorizontal);

    Sprite getLever(boolean on);
}
