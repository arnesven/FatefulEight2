package model.tasks;

import model.Model;
import model.actions.DailyAction;
import model.journal.JournalEntry;
import model.map.CastleLocation;
import model.map.wars.KingdomWar;
import model.states.dailyaction.CommandOutpostDailyAction;

import java.awt.*;

public class BattleDestinationTask extends DestinationTask {
    private final KingdomWar war;
    private final String givenBy;
    private final boolean givenByAggressor;
    private final String enemy;
    private boolean completed = false;
    private boolean success = true;

    public BattleDestinationTask(KingdomWar war, CastleLocation castle) {
        super(new Point(war.getBattlePosition(castle)), "Battle " + war.getCurrentBattleName());
        this.war = war;
        this.givenBy = castle.getPlaceName();
        this.givenByAggressor = war.isAggressor(castle);
        this.enemy = givenByAggressor ? war.getDefender() : war.getAggressor();
    }

    public boolean isForKingdom(CastleLocation castle) {
        return givenBy.equals(castle.getPlaceName());
    }

    @Override
    public JournalEntry getJournalEntry(Model model) {
        return new BattleJournalEntry(model, getPosition());
    }

    @Override
    public JournalEntry getFailedJournalEntry(Model model) {
        return new BattleJournalEntry(model, getPosition());
    }

    @Override
    public DailyAction getDailyAction(Model model) {
        return new CommandOutpostDailyAction(model, war, givenByAggressor, this);
    }

    @Override
    public boolean isFailed(Model model) {
        return !success;
    }

    @Override
    public boolean givesDailyAction(Model model) {
        if (isCompleted() || isFailed(model)) {
            return false;
        }
        CastleLocation castle = (CastleLocation) model.getWorld().getUrbanLocationByPlaceName(givenBy);
        return model.getParty().getPosition().equals(war.getBattlePosition(castle));
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean b) {
        this.completed = b;
    }

    public void setSuccess(boolean b) {
        this.success = b;
    }

    private class BattleJournalEntry implements JournalEntry {
        private final Point position;

        public BattleJournalEntry(Model model, Point position) {
            this.position = position;
        }

        @Override
        public String getName() {
            return getDestinationDescription();
        }

        @Override
        public String getText() {
            if (isComplete()) {
                if (isFailed()) {
                    return "You were defeated in battle against the force of the " +
                            CastleLocation.placeNameToKingdom(enemy) + ".";
                }
                return "You were victorious in battle against the forces of the " +
                        CastleLocation.placeNameToKingdom(enemy) + ".";
            }
            return "Talk to the field general at the command outpost about " +
                    "helping out in the upcoming battle against the " + CastleLocation.placeNameToKingdom(enemy) + ".";
        }

        @Override
        public boolean isComplete() {
            return isCompleted();
        }

        @Override
        public boolean isFailed() {
            return !success;
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
