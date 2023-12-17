package model.ruins.objects;

public abstract class DungeonDoor extends DungeonObject {

    private DungeonDoor otherDoor;

    public DungeonDoor(int x, int y) {
        super(x, y);
    }

    public void link(DungeonDoor door) {
        this.otherDoor = door;
    }

    public DungeonDoor getLinkedDoor() {
        return otherDoor;
    }
}
