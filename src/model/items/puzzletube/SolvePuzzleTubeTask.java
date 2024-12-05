package model.items.puzzletube;

import model.Model;
import model.journal.JournalEntry;

import java.awt.*;

class SolvePuzzleTubeTask implements JournalEntry {
    private final WordPuzzle puzzle;
    private final DwarvenPuzzleTube tube;

    public SolvePuzzleTubeTask(DwarvenPuzzleTube dwarvenPuzzleTube, WordPuzzle puzzle) {
        this.tube = dwarvenPuzzleTube;
        this.puzzle = puzzle;
    }

    @Override
    public String getName() {
        return "Solve " + tube.getName();
    }

    @Override
    public String getText() {
        if (isComplete()) {
            return "You solved " + tube.getName() + " on day " + puzzle.getSolvedOnDay() + ".\n" +
                    "\nThe word was " + puzzle.getSolutionWord() + ".\n\n" +
                    "Completed";
        }
        return "You have found a Dwarven Puzzle Tube. Perhaps there is some " +
                "kind of treasure inside, if only you can get it open...";
    }

    @Override
    public boolean isComplete() {
        return puzzle.isParchmentRemoved();
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
        return null;
    }
}
