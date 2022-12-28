package model.quests;

import view.MyColors;

import java.io.Serializable;

public abstract class QuestSubScene extends QuestNode implements Serializable {

    private final int col;
    private final int row;

    private QuestEdge failConnection;
    private QuestEdge successConnection;

    public QuestSubScene(int col, int row) {
        this.col = col;
        this.row = row;
    }

    @Override
    public int getColumn() {
        return col;
    }

    @Override
    public int getRow() {
        return row;
    }

    public void connectFail(QuestNode questNode) {
        this.failConnection = new QuestEdge(questNode, QuestEdge.HORIZONTAL, MyColors.LIGHT_RED);
    }

    public void connectSuccess(QuestNode questNode, boolean align) {
        this.successConnection = new QuestEdge(questNode, align, getSuccessEdgeColor());
    }

    protected abstract MyColors getSuccessEdgeColor();

    public void connectSuccess(QuestNode questNode) {
        connectSuccess(questNode, QuestEdge.HORIZONTAL);
    }

    public QuestEdge getSuccessEdge() {
        return successConnection;
    }

    public QuestEdge getFailEdge() {
        return failConnection;
    }
}
