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

public abstract class GainSupportOfNeighborKingdomTask extends DestinationTask {
    private final String neighbor;
    private final String otherNeighbor;
    private final String kingdom;
    private final Point battlePosition;
    private boolean completed;
    private int stage = 0;

    public GainSupportOfNeighborKingdomTask(String neighbor, Point position, String kingdom, String otherNeighbor,
                                            Point battlePosition) {
        super(position, "");
        this.neighbor = neighbor;
        this.kingdom = kingdom;
        this.otherNeighbor = otherNeighbor;
        this.completed = false;
        this.battlePosition = battlePosition;
    }

    @Override
    public Point getPosition() {
        if (stage == 0) {
            return super.getPosition();
        }
        return battlePosition;
    }

    protected abstract GameState makeBattleEvent(Model model, CastleLocation defender, CastleLocation invader);

    protected abstract VisitLordEvent innerMakeLordEvent(Model model, UrbanLocation neighbor, CastleLocation kingdom);

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
        CastleLocation defender = model.getWorld().getCastleByName(neighbor);
        CastleLocation invader = model.getWorld().getCastleByName(kingdom); // TODO: Not if already completed!
        return new DailyAction("Go to War Camp", makeBattleEvent(model, defender, invader));
    }

    @Override
    public boolean isFailed(Model model) {
        return false;
    }

    @Override
    public boolean givesDailyAction(Model model) {
        return stage > 0 && !isCompleted();
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }

    public boolean isAtLocation(Model model, UrbanLocation location) {
        return model.getWorld().getCastleByName(neighbor) == location;
    }

    public void setCompleted(boolean b) {
        this.completed = b;
    }

    public VisitLordEvent makeLordEvent(Model model, UrbanLocation neighbor) {
        return innerMakeLordEvent(model, neighbor, model.getWorld().getCastleByName(otherNeighbor));
    }

    public CastleLocation getKingdom(Model model) {
        return model.getWorld().getCastleByName(neighbor);
    }

    public void progress() {
        this.stage++;
    }

    public int getStage() {
        return stage;
    }

    private class GainSupportOfKingdomJournalEntry extends MainStoryTask {
        public GainSupportOfKingdomJournalEntry() {
            super("Gain the support of " + CastleLocation.placeNameShort(neighbor));
        }

        @Override
        public String getText() {
            if (stage == 0) {
                return "Travel to the " + CastleLocation.placeNameToKingdom(neighbor) + " and gain their support " +
                        "to overthrow the mad ruler of " + kingdom + ".";
            }
            return "Aid " + CastleLocation.placeNameShort(neighbor) + " in their fight against an invading force."
                    + (isComplete() ? ".\n\nCompleted" : "");
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
