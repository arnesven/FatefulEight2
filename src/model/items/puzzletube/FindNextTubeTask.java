package model.items.puzzletube;

import model.Model;
import model.journal.JournalEntry;
import model.tasks.Destination;

import java.awt.*;

class FindNextTubeTask implements JournalEntry {
    private final Destination destination;
    private final boolean completed;

    public FindNextTubeTask(Destination destination, boolean completed) {
        this.destination = destination;
        this.completed = completed;
    }

    @Override
    public String getName() {
        return "Find another Puzzle Tube";
    }

    @Override
    public String getText() {
        if (completed) {
            return "You found the Dwarven Puzzle Tube " + destination.getPreposition() + " " +
                    destination.getLongDescription() + ".\n\nCompleted";
        }
        return "Search for another Dwarven Puzzle Tube. According to the parchment, it is " +
                destination.getPreposition() + " " +
                destination.getLongDescription() + ".";
    }

    @Override
    public boolean isComplete() {
        return completed;
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
        return destination.getPosition();
    }
}
