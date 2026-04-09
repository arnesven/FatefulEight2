package model.states.mine;

public class BreakableRockMineObject extends RockMineObject {
    public BreakableRockMineObject() {
        super(true);
    }

    @Override
    public MineObject copy() {
        return new BreakableRockMineObject();
    }
}
