package model.quests.scenes;

import model.quests.QuestEdge;
import model.quests.QuestNode;

public class ArrowlessEdge extends QuestEdge {
    public ArrowlessEdge(QuestNode questNode) {
        super(questNode);
    }

    @Override
    protected boolean drawArrow() {
        return false;
    }
}
