package model.mainstory;

import model.Model;
import model.actions.DailyAction;
import model.journal.JournalEntry;
import model.journal.MainStoryTask;
import model.map.CastleLocation;
import model.map.UrbanLocation;
import model.states.GameState;
import model.tasks.DestinationTask;

import java.awt.*;

public class GainSupportOfNeighborKingdomTask extends DestinationTask {
    private final String neighbor;
    private final String kingdom;
    private boolean completed;

    public GainSupportOfNeighborKingdomTask(String neighbor, Point position, String kingdom) {
        super(position, "");
        this.neighbor = neighbor;
        this.kingdom = kingdom;
        this.completed = false;
    }

    @Override
    public JournalEntry getJournalEntry(Model model) {
        return new GainSupportOfKingdomJournalEntry();
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
        return completed;
    }

    public boolean isAtLocation(Model model, UrbanLocation location) {
        return model.getWorld().getCastleByName(neighbor) == location;
    }

    public void setCompleted(boolean b) {
        this.completed = true;
    }

    private class GainSupportOfKingdomJournalEntry extends MainStoryTask {
        public GainSupportOfKingdomJournalEntry() {
            super("Gain the support of " + CastleLocation.placeNameShort(neighbor));
        }

        @Override
        public String getText() {
            return "Travel to the " + CastleLocation.placeNameToKingdom(neighbor) + " and gain their support " +
                    "to overthrow the mad ruler of " + kingdom + ".\n\n" + (isComplete() ? "Completed" : "");
        }

        @Override
        public boolean isComplete() {
            return GainSupportOfNeighborKingdomTask.this.isCompleted();
        }

        @Override
        public Point getPosition(Model model) {
            return GainSupportOfNeighborKingdomTask.this.getPosition();
        }
    }
}
