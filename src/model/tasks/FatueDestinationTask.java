package model.tasks;

import model.Model;
import model.actions.DailyAction;
import model.journal.JournalEntry;
import model.map.UrbanLocation;
import model.states.fatue.FortressAtUtmostEdgeState;

import java.awt.*;

public class FatueDestinationTask extends DestinationTask {
    private final String townOrCastleName;
    private final Point position;

    public FatueDestinationTask(Point position, UrbanLocation townOrCastle) {
        super(position, "Find the strange fortress in the caves around " + townOrCastle.getPlaceName() + ".");
        this.position = position;
        this.townOrCastleName = townOrCastle.getPlaceName();
    }

    @Override
    public boolean drawTaskOnMap(Model model) {
        return false;
    }

    @Override
    public JournalEntry getJournalEntry(Model model) {
        return new FatueJournalEntry(FortressAtUtmostEdgeState.firstTimeInCastleProper(model),
                FortressAtUtmostEdgeState.hasStaff(model),
                FortressAtUtmostEdgeState.isCleared(model),
                FortressAtUtmostEdgeState.getNumberOfPiecesOfStaffFound(model)
                );
    }

    @Override
    public JournalEntry getFailedJournalEntry(Model model) {
        return getJournalEntry(model);
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

    private class FatueJournalEntry implements JournalEntry {

        private final int stage;
        private final int piecesFound;

        public FatueJournalEntry(boolean found, boolean hasStaff, boolean isCleared, int numberOfPiecesOfStaffFound) {
            stage = (found ? 1 : 0) + (hasStaff ? 1 : 0) + (isCleared ? 1 : 0);
            this.piecesFound = numberOfPiecesOfStaffFound;
        }

        @Override
        public String getName() {
            return "Fortress in the Caves";
        }

        @Override
        public String getText() {
            switch (stage) {
                case 1:
                    return "Find and investigate the strange fortress in the caves near " + townOrCastleName + ".";
                case 2:
                    if (piecesFound == FortressAtUtmostEdgeState.NUMBER_OF_PIECES) {
                        return "Return to the center of the Fortress at the Utmost Edge and assemble the pieces of " +
                                "the Staff of Deimos, so you can unlock the large stone door.";
                    }
                    return "Collect the pieces of the Staff of Deimos to unlock the " +
                            "locked stone door in the Fortress at the Utmost Edge.\nYou have found " +
                            piecesFound + " pieces so far.";
                default:
                    return "You defeated the Ultimate Adversary in the Fortress at the Utmost Edge.\n\nCompleted.";
            }
        }

        @Override
        public boolean isComplete() {
            return stage == 3;
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
            return position;
        }
    }
}
