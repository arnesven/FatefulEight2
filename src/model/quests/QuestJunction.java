package model.quests;

import model.Model;
import model.states.GameState;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public abstract class QuestJunction extends QuestNode {

    private final int column;
    private final int row;
    private List<QuestNode> connections;

    public QuestJunction(int column, int row) {
        this.column = column;
        this.row = row;
        connections = new ArrayList<>();
    }

    @Override
    public int getColumn() {
        return column;
    }

    @Override
    public int getRow() {
        return row;
    }

    public QuestNode getConnection(int index) {
        return connections.get(0);
    }

    public void connectTo(QuestNode questNode) {
        this.connections.add(questNode);
    }

    protected List<QuestNode> getConnections() {
        return connections;
    }
}
