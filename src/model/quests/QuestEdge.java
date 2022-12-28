package model.quests;

import java.io.Serializable;

public class QuestEdge implements Serializable {
    public static final boolean HORIZONTAL = true;
    private QuestNode node;
    private boolean alignment;

    public QuestEdge(QuestNode node, boolean align) {
        this.node = node;
        this.alignment = align;
    }

    public QuestEdge(QuestNode node) {
        this(node, QuestEdge.HORIZONTAL);
    }

    public QuestNode getNode() {
        return node;
    }
}
