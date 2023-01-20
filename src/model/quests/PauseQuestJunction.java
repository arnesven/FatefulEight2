package model.quests;

import model.Model;
import model.states.QuestState;

public class PauseQuestJunction extends SimpleJunction {
    private final String leaderTalk;

    public PauseQuestJunction(int col, int row, QuestEdge connection, String leaderTalk) {
        super(col, row, connection);
        this.leaderTalk = leaderTalk;
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public QuestEdge run(Model model, QuestState state) {
        if (!leaderTalk.equals("")) {
            model.getParty().partyMemberSay(model, model.getParty().getLeader(), leaderTalk);
        }
        state.print("Press enter to continue.");
        state.waitForReturn();
        return super.run(model, state);
    }
}
