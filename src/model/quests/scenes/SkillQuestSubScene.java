package model.quests.scenes;

import model.Model;
import model.quests.QuestSubScene;
import view.MyColors;

public abstract class SkillQuestSubScene extends QuestSubScene {

    public SkillQuestSubScene(int col, int row) {
        super(col, row);
    }

    @Override
    protected MyColors getSuccessEdgeColor() {
        return MyColors.LIGHT_GREEN;
    }

    protected void leaderSay(Model model, String text) {
        if (!text.equals("")) {
            model.getParty().partyMemberSay(model, model.getParty().getLeader(), text);
        }
    }
}
