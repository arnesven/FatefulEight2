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

    public QuestDecisionPoint(int column, int row, List<QuestNode> connections) {
        super(column, row);
        for (QuestNode q : connections) {
            connectTo(q);
        }
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
