package model.states.dailyaction;

public abstract class InsideDailyActionNode extends DailyActionNode {
    public InsideDailyActionNode(String name) {
        super(name);
    }

    @Override
    public boolean blocksPassage() {
        return false;
    }
}
