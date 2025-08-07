package model.ruins;


public class UpperRightCornerCorridor extends HorizontalCorridorRoom {
    public UpperRightCornerCorridor(DungeonRoom dungeonRoom) {
        super(dungeonRoom);
    }

    @Override
    public int getMapRoomSpriteNumber() {
        return 0x157;
    }

    @Override
    public boolean hasLowerLeftCorner() {
        return true;
    }
}
