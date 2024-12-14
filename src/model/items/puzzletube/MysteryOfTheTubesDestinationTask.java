package model.items.puzzletube;

import model.Model;
import model.actions.DailyAction;
import model.journal.JournalEntry;
import model.map.CastleLocation;
import model.tasks.Destination;
import model.tasks.DestinationTask;

import java.awt.*;

public class MysteryOfTheTubesDestinationTask extends DestinationTask {

    private static final int INITIAL_STEP = 0;
    private static final int KINGDOM_FOUND_OUT = 1;
    private static final int TOYMAKER_HUT_FOUND_OUT = 2;
    private final Destination destination;

    private int step = 0;

    public MysteryOfTheTubesDestinationTask(Model model, Point position) {
        super(position, "");
        this.destination = Destination.makeDestination(model, position, "house", "in");
    }

    public boolean isInKingdom(Model model) {
        CastleLocation kingdomForToymaker = model.getWorld().getKingdomForPosition(getPosition());
        return kingdomForToymaker == model.getWorld().getKingdomForPosition(model.getParty().getPosition());
    }

    @Override
    public JournalEntry getJournalEntry(Model model) {
        CastleLocation kingdomCastle = model.getWorld().getKingdomForPosition(getPosition());
        return new MysteryJournalEntry(kingdomCastle);
    }

    @Override
    public boolean drawTaskOnMap(Model model) {
        return step == TOYMAKER_HUT_FOUND_OUT;
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

    public void progressToHutFound() {
        if (step < TOYMAKER_HUT_FOUND_OUT) {
            step = TOYMAKER_HUT_FOUND_OUT;
        }
    }

    public void progressToKingdomKnown() {
        if (step < KINGDOM_FOUND_OUT) {
            step = KINGDOM_FOUND_OUT;
        }
    }

    private class MysteryJournalEntry implements JournalEntry {
        private final CastleLocation kingdomCastle;

        public MysteryJournalEntry(CastleLocation kingdomCastle) {
            this.kingdomCastle = kingdomCastle;
        }

        @Override
        public String getName() {
            return "Mystery of the Puzzle Tubes";
        }

        @Override
        public String getText() {
            String extra =  "You may have to ask around a bit to gain more " +
                            "clues about the origin of these enigmatic trinkets.";
            if (step > INITIAL_STEP) {
                extra = "The Puzzle Tubes were made by a dwarf, an eccentric toy maker from the " +
                            CastleLocation.placeNameToKingdom(kingdomCastle.getPlaceName()) + ".";
                if (step == TOYMAKER_HUT_FOUND_OUT) {
                    extra += " His name is " + DwarvenPuzzleConstants.TOYMAKER_NAME + ". ";
                    extra += " His last known residence was " +
                            destination.getLongDescription() + ".";
                }
            }
            return "Unravel the mystery of the Dwarven Puzzle Tubes. Who made them and what is their purpose?\n\n" +
                    extra;
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
            if (step == TOYMAKER_HUT_FOUND_OUT) {
                return MysteryOfTheTubesDestinationTask.this.getPosition();
            }
            return null;
        }
    }
}
