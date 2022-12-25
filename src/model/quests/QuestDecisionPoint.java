package model.quests;

import model.Model;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.states.GameState;
import model.states.QuestState;

public abstract class QuestDecisionPoint extends QuestJunction {

    public QuestDecisionPoint(int column, int row) {
        super(column, row);
    }

    @Override
    public String getDescription() {
        return "Leader decision point: Solo Leadership 7";
    }

    public QuestNode questNodeInput(QuestState state) {
        do {
            state.print("Please select which sub-scene to advance to.");
            state.waitForReturn();
            if (super.getConnections().contains(state.getSelectedElement())) {
                return state.getSelectedElement();
            }
            state.println("The selected sub-scene is not reachable from your current position.");
        } while (true);
    }

    @Override
    public QuestNode run(Model model, QuestState state) {
        SkillCheckResult result = model.getParty().getLeader().testSkill(Skill.Leadership, 6);
        state.println("Party leader " + model.getParty().getLeader().getFirstName() + " tests Leadership " + result.asString());
        if (!result.isSuccessful()) {
            return getConnection(0);
        }
        return questNodeInput(state);
    }
}
