package model.quests;

import java.util.List;

public class QuestStartingPointWithPosition extends QuestStartPoint {
    public QuestStartingPointWithPosition(int col, int row, List<QuestEdge> questEdges, String leaderTalk) {
        super(questEdges, leaderTalk);
        setColumn(col);
        setRow(row);
    }
}
