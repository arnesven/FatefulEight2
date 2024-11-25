package model.quests.scenes;

import model.Model;
import model.quests.QuestEdge;
import model.quests.QuestNode;
import model.states.QuestState;

public class DummyQuestNode extends QuestNode {
    private final int column;
    private final int row;

    public DummyQuestNode(int col, int row) {
        this.column = col;
        this.row = row;
    }

    @Override
    public int getColumn() {
        return column;
    }

    @Override
    public int getRow() {
        return row;
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public QuestEdge run(Model model, QuestState state) {
        return null;
    }
}
