package model.tasks;

import model.Model;
import model.actions.DailyAction;
import model.journal.JournalEntry;

import java.awt.*;
import java.io.Serializable;

public abstract class DestinationTask implements Serializable {
    private final Point position;
    private final String description;

    public DestinationTask(Point position, String description) {
        this.position = position;
        this.description = description;
    }

    public String getDestinationDescription() {
        return description;
    }

    public Point getPosition() {
        return position;
    }

    public abstract JournalEntry getJournalEntry(Model model);

    public abstract JournalEntry getFailedJournalEntry(Model model);

    public abstract DailyAction getDailyAction(Model model);

    public abstract boolean isFailed(Model model);

    public abstract boolean givesDailyAction(Model model);

    public abstract boolean isCompleted();

    public boolean drawTaskOnMap(Model model) {
        return true;
    }
}
