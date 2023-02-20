package model.quests;

import model.Model;
import model.states.QuestState;

public class CountingJunction extends SimpleJunction {
    public CountingJunction(int col, int row, QuestEdge edge) {
        super(col, row, edge);
    }

    @Override
    public QuestEdge run(Model model, QuestState state) {
        state.increaseQuestCounter();
        return super.run(model, state);
    }
}
