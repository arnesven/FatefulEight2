package model.quests;

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

    public void connectFail(QuestEdge questNode) {
        this.failConnection = questNode;
    }

    public void connectSuccess(QuestEdge questNode) {
        this.successConnection = questNode;
    }

    public QuestNode getSuccessConnection() {
        return successConnection.getNode();
    }

    public QuestNode getFailConnection() {
        return failConnection.getNode();
    }
}
