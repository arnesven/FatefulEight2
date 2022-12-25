package model.quests;

import java.io.Serializable;

public abstract class QuestSubScene extends QuestNode implements Serializable {

    private final int col;
    private final int row;

    private QuestNode failConnection;
    private QuestNode successConnection;

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
        this.failConnection = questNode;
    }

    public void connectSuccess(QuestNode questNode) {
        this.successConnection = questNode;
    }

    public QuestNode getSuccessConnection() {
        return successConnection;
    }

    public QuestNode getFailConnection() {
        return failConnection;
    }
}
