package model.ruins;

import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public interface DungeonTheme {
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
}
