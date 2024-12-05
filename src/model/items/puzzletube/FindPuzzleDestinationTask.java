package model.items.puzzletube;

import model.Model;
import model.actions.DailyAction;
import model.journal.JournalEntry;
import model.states.dailyaction.SearchForDwarvenTubeAction;
import model.states.events.FindPuzzleTubeEvent;
import model.tasks.Destination;
import model.tasks.DestinationTask;

public class FindPuzzleDestinationTask extends DestinationTask {
    private final Destination destination;
    private boolean completed = false;

    public FindPuzzleDestinationTask(Destination nextDestination) {
        super(nextDestination.getPosition(), nextDestination.getShortDescription());
        this.destination = nextDestination;
    }

    @Override
    public JournalEntry getJournalEntry(Model model) {
        return new FindNextTubeTask(destination, isCompleted());
    }

    @Override
    public JournalEntry getFailedJournalEntry(Model model) {
        return null;
    }

    @Override
    public DailyAction getDailyAction(Model model) {
        return new SearchForDwarvenTubeAction(model, destination, this);
    }

    @Override
    public boolean isFailed(Model model) {
        return false;
    }

    @Override
    public boolean givesDailyAction(Model model) {
        completed = FindPuzzleTubeEvent.alreadyFoundInThisLocation(model);
        return !completed && model.getParty().getPosition().equals(destination.getPosition());
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean b) {
        this.completed = b;
    }
}
