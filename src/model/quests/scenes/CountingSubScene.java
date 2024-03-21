package model.quests.scenes;

import model.Model;
import model.quests.CountingQuest;
import model.quests.QuestEdge;
import model.quests.QuestNode;
import model.quests.QuestSubScene;
import model.states.QuestState;
import view.MyColors;

public class CountingSubScene extends QuestSubScene {
    private final QuestSubScene inner;
    private final CountingQuest countQuest;

    public CountingSubScene(CountingQuest countQuest, QuestSubScene inner) {
        super(inner.getColumn(), inner.getRow());
        this.inner = inner;
        this.countQuest = countQuest;
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
        inner.drawYourself(model, xPos, yPos);
    }

    @Override
    public String getDescription() {
        return "*" + inner.getDescription();
    }

    @Override
    public QuestEdge run(Model model, QuestState state) {
        QuestEdge innerResult = inner.run(model, state);
        if (innerResult == inner.getSuccessEdge()) {
            countQuest.addToCount(1);
        }
        return getSuccessEdge();
    }

    @Override
    public void connectSuccess(QuestNode questNode, boolean align) {
        super.connectSuccess(questNode, align);
        inner.connectSuccess(questNode, align);
    }

    @Override
    protected MyColors getSuccessEdgeColor() {
        return MyColors.WHITE;
    }

    @Override
    public String getDetailedDescription() {
        return inner.getDetailedDescription();
    }
}
