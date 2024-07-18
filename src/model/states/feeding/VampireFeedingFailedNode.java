package model.states.feeding;

import model.quests.QuestFailNode;

public class VampireFeedingFailedNode extends QuestFailNode {

    @Override
    public int getRow() {
        return 5;
    }

    @Override
    public int getColumn() {
        return 0;
    }
}
