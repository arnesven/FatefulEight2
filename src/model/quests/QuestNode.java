package model.quests;

import model.Model;
import model.states.QuestState;

import java.io.Serializable;

public abstract class QuestNode implements Serializable {
    public abstract int getColumn();
    public abstract int getRow();
    public abstract void drawYourself(Model model, int xPos, int yPos);
    public abstract String getDescription();
    public abstract QuestNode run(Model model, QuestState state);
}
