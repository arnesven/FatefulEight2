package model.quests.scenes;

import model.Model;
import model.quests.QuestDecisionPoint;
import model.quests.QuestEdge;
import model.states.DailyEventState;
import model.states.QuestState;

import java.util.List;

public abstract class DecisionJunctionWithEvent extends QuestDecisionPoint {
    public DecisionJunctionWithEvent(int col, int row, List<QuestEdge> questEdges) {
        super(col, row, questEdges, "");
    }

    @Override
    public String getDescription() {
        return "Leader decision point";
    }

    @Override
    protected boolean canTakeDecision(Model model, QuestState state) {
        makeEvent(model, state).doTheEvent(model);
        return true;
    }

    public abstract DailyEventState makeEvent(Model model, QuestState state);
}
