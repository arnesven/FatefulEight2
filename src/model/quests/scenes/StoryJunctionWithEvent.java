package model.quests.scenes;

import model.Model;
import model.quests.QuestEdge;
import model.quests.StoryJunction;
import model.states.DailyEventState;
import model.states.QuestState;

public abstract class StoryJunctionWithEvent extends StoryJunction {

    public StoryJunctionWithEvent(int col, int row, QuestEdge edge) {
        super(col, row, edge);
    }

    @Override
    protected void doAction(Model model, QuestState state) {
        makeEvent(model, state).doTheEvent(model);
    }

    public abstract DailyEventState makeEvent(Model model, QuestState state);
}
