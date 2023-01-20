package model.quests;

import model.Model;
import model.states.QuestState;

import java.awt.*;
import java.io.Serializable;

public abstract class QuestNode implements Serializable {
    public abstract int getColumn();
    public abstract int getRow();
    public abstract void drawYourself(Model model, int xPos, int yPos);
    public abstract String getDescription();
    public abstract QuestEdge run(Model model, QuestState state);

    public Point getPosition() {
        return new Point(getColumn(), getRow());
    }

    protected boolean isEligibleForSelection(Model model, QuestState state) {
        return true;
    }
}
