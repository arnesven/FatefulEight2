package model.tasks;

import model.Model;
import model.actions.DailyAction;
import model.journal.JournalEntry;
import model.states.events.GentlepersonsClubEvent;

import java.awt.*;

public class JoinGentlepersonsClubTask extends DestinationTask {
    private boolean completed = false;

    public JoinGentlepersonsClubTask(Model model) {
        super(new Point(model.getParty().getPosition()),
                "The Gentleperson's club is an exclusive sorority for aristocrats, " +
                        "artists, actors, musicians or anybody who appreciates " +
                        "the aesthetics of the finer points of life. You should join it.");
    }

    @Override
    public JournalEntry getJournalEntry(Model model) {
        return new JoinGentlepersonsClubEntry();
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @Override
    public JournalEntry getFailedJournalEntry(Model model) {
        return new JoinGentlepersonsClubEntry();
    }

    @Override
    public DailyAction getDailyAction(Model model) {
        return new GoToClubDailyAction(model);
    }

    @Override
    public boolean isFailed(Model model) {
        return false;
    }

    @Override
    public boolean givesDailyAction(Model model) {
        return !isCompleted();
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }

    private class JoinGentlepersonsClubEntry implements JournalEntry {
        @Override
        public String getName() {
            return "Join the Gentleperson's Club";
        }

        @Override
        public String getText() {
            return JoinGentlepersonsClubTask.this.getDestinationDescription() +
                    (isCompleted() ? "\n\nCompleted": "");
        }

        @Override
        public boolean isComplete() {
            return JoinGentlepersonsClubTask.this.isCompleted();
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
            return JoinGentlepersonsClubTask.this.getPosition();
        }
    }

    private static class GoToClubDailyAction extends DailyAction {
        public GoToClubDailyAction(Model model) {
            super("Go to club", new GentlepersonsClubEvent(model));
        }
    }
}
