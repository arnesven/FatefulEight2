package model.mainstory;

import model.Model;
import model.actions.DailyAction;
import model.journal.JournalEntry;
import model.tasks.DestinationTask;

import java.awt.*;

public abstract class GainSupportOfRemotePeopleTask extends DestinationTask {
    public GainSupportOfRemotePeopleTask(Point position) {
        super(position, "");
    }

    @Override
    public JournalEntry getFailedJournalEntry(Model model) {
        return null;
    }

    @Override
    public DailyAction getDailyAction(Model model) {
        return null;
    }

    @Override
    public boolean isFailed(Model model) {
        return false;
    }

    @Override
    public boolean givesDailyAction(Model model) {
        return false;
    }
}
