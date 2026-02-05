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
    private static final int RESIDENCE_FOUND_OUT = 2;
    private static final int HUT_FOUND = 3;
    private final Destination destination;

    private int step = 0;

    public MysteryOfTheTubesDestinationTask(Model model, Point position) {
        super(position, "");
        this.destination = Destination.makeDestination(model, position, "house", "in");
    }

    public boolean isInKingdom(Model model) {
        if (!model.isInOriginalWorld()) {
            return false;
        }
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
        return step == RESIDENCE_FOUND_OUT;
    }

    @Override
    public JournalEntry getFailedJournalEntry(Model model) {
        return null;
    }

    @Override
    public DailyAction getDailyAction(Model model) {
        return new DailyAction("Search for toy maker's house",
                new SearchForToyMakersHouseEvent(model, this));
    }

    @Override
    public boolean isFailed(Model model) {
        return false;
    }

    @Override
    public boolean givesDailyAction(Model model) {
        return step == RESIDENCE_FOUND_OUT && model.partyIsInOverworldPosition(getPosition());
    }

    @Override
    public boolean isCompleted() {
        return false;
    }

    @Override
    public Point getPosition() {
        if (step < HUT_FOUND) {
            return super.getPosition();
        }
        Point shifted = super.getPosition();
        shifted.y = shifted.y - 1;
        return shifted;
    }

    public void progressToResidenceKnown() {
        if (step < RESIDENCE_FOUND_OUT) {
            step = RESIDENCE_FOUND_OUT;
        }
    }

    public void progressToKingdomKnown() {
        if (step < KINGDOM_FOUND_OUT) {
            step = KINGDOM_FOUND_OUT;
        }
    }

    public void progressToHutFound() {
        if (step < HUT_FOUND) {
            step = HUT_FOUND;
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
                if (step == KINGDOM_FOUND_OUT) {
                    extra += "\n\nMaybe somebody in that kingdom knows more.";
                } else if (step == RESIDENCE_FOUND_OUT) {
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
            if (step == RESIDENCE_FOUND_OUT) {
                return MysteryOfTheTubesDestinationTask.this.getPosition();
            }
            return null;
        }
    }
}
