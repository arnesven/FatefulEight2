package model.journal;

import model.Model;
import model.states.GameState;
import model.states.InitialLeadsEveningState;

public interface JournalEntry {
    String getName();
    String getText();
    boolean isComplete();
    boolean isFailed();
    boolean isTask();

    static void printJournalUpdateMessage(Model model) {
        model.getLog().addAnimated("!Your journal has been updated!\n");
    }

    static void printMapExpandedMessage(Model model) {
        model.getLog().addAnimated("!Your map has been expanded! You should check the map view.\n");
    }
}
