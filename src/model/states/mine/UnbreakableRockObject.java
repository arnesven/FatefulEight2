package model.states.mine;

public class UnbreakableRockObject extends RockMineObject {
    public UnbreakableRockObject() {
        super(false);
    }

    @Override
    public MineObject copy() {
        return new UnbreakableRockObject();
    }
}
