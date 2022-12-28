package model.quests;

import java.util.ArrayList;
import java.util.List;

public abstract class QuestJunction extends QuestNode {

    private final int column;
    private final int row;
    private List<QuestEdge> connections;

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

    public QuestEdge getConnection(int index) {
        return connections.get(index);
    }

    public void connectTo(QuestEdge edge) {
        this.connections.add(edge);
    }

    public List<QuestEdge> getConnections() {
        return connections;
    }

    protected List<QuestNode> getConnectedNodes() {
        List<QuestNode> nodes = new ArrayList<>();
        for (QuestEdge e : connections) {
            nodes.add(e.getNode());
        }
        return nodes;
    }
}
