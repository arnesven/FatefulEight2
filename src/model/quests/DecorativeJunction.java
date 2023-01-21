package model.quests;

import model.Model;
import model.states.QuestState;

public abstract class DecorativeJunction extends QuestJunction {
    public DecorativeJunction(int column, int row) {
        super(column, row);
    }

    @Override
    public QuestEdge run(Model model, QuestState state) {
        throw new IllegalStateException("This method should never be called!");
    }
}
