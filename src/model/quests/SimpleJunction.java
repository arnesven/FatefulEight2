package model.quests;

import model.Model;
import model.states.QuestState;

import java.util.List;

public class SimpleJunction extends QuestJunction {
    public SimpleJunction(int col, int row, List<QuestSubScene> connections) {
        super(col, row);
        for (QuestNode q : connections) {
            connectTo(q);
        }
    }

    @Override
    public void drawYourself(Model model, int xPos, int yPos) {

    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public QuestNode run(Model model, QuestState state) {
        return null;
    }
}
