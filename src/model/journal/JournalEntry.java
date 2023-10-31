package model.journal;

import model.Model;
import model.states.GameState;
import model.states.InitialLeadsEveningState;
import view.LogView;

public interface JournalEntry {
    String getName();
    String getText();
    boolean isComplete();
    boolean isFailed();
    boolean isTask();

    static void printJournalUpdateMessage(Model model) {
        model.getLog().addAnimated(LogView.GOLD_COLOR + "Your journal has been updated!\n" + LogView.DEFAULT_COLOR);
    }

    static void printMapExpandedMessage(Model model) {
        model.getLog().addAnimated(LogView.GOLD_COLOR + "Your map has been expanded! You should check the map view.\n" + LogView.DEFAULT_COLOR);
    }
}
