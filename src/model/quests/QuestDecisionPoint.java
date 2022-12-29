package model.quests;

import model.Model;
import model.classes.Skill;
import model.classes.SkillCheckResult;
import model.states.GameState;
import model.states.QuestState;
import view.MyColors;
import view.sprites.Sprite32x32;

import java.awt.*;
import java.util.List;

public class QuestDecisionPoint extends QuestJunction {

    private final String leaderTalk;

    public QuestDecisionPoint(int column, int row, List<QuestEdge> connections, String leaderTalk) {
        super(column, row);
        for (QuestEdge c : connections) {
            connectTo(c);
        }
        this.leaderTalk = leaderTalk;
    }

    private final Sprite32x32 SPRITE = new Sprite32x32("decisionpoint", "quest.png", 0x12, MyColors.BLACK, MyColors.WHITE, MyColors.RED);

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
        model.getScreenHandler().register(SPRITE.getName(), new Point(xPos, yPos), SPRITE);
    }


    @Override
    public String getDescription() {
        return "Leader decision point: Solo Leadership 6";
    }

    public QuestEdge questNodeInput(QuestState state) {
        state.setSelectedElement(getConnections().get(0).getNode());
        do {
            state.print("Please select which location to advance to.");
            state.waitForReturn(true);
            for (QuestEdge edge : getConnections()) {
                if (edge.getNode() == state.getSelectedElement()) {
                    return edge;
                }
            }
            state.println("The selected location is not reachable from your current position.");
        } while (true);
    }

    @Override
    public QuestEdge run(Model model, QuestState state) {
        if (!leaderTalk.equals("")) {
            model.getParty().partyMemberSay(model, model.getParty().getLeader(), leaderTalk);
            model.getLog().waitForAnimationToFinish();
        }
        SkillCheckResult result = model.getParty().getLeader().testSkill(Skill.Leadership, 1); // TODO: 6
        state.println("Party leader " + model.getParty().getLeader().getFirstName() + " tests Leadership " + result.asString());
        model.getLog().waitForAnimationToFinish();
        if (!result.isSuccessful()) {
            return getConnection(0);
        }
        return questNodeInput(state);
    }
}
