package model.mainstory.thepast;

import model.Model;
import model.actions.DailyAction;
import model.journal.JournalEntry;
import model.tasks.DestinationTask;

import java.awt.*;

public class FindArabellaTask extends PastWorldDestinationTask {
    public FindArabellaTask(Point p) {
        super(p, "Find Arabella");
    }

    @Override
    public JournalEntry getJournalEntry(Model model) {
        return new ThePastJournalEntry() {
            @Override
            public String getName() {
                return getDestinationDescription();
            }

            @Override
            public String getText() {
                return "The fact that Arabella roams free and could link up with the Quad worries you.\n\n" +
                        "You must find her and stop her.";
            }

            @Override
            public boolean isComplete() {
                return false;
            }

            @Override
            public boolean isFailed() {
                return false;
            }

            @Override
            public boolean isTask() {
                return true;
            }

            @Override
            public Point getPosition(Model model) {
                return FindArabellaTask.this.getPosition();
            }
        };
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

    @Override
    public boolean isCompleted() {
        return false;
    }
}
