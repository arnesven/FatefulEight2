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

    public BattleDestinationTask(KingdomWar war, CastleLocation castle) {
        super(war.getBattlePosition(castle), "Battle TODO");
        this.war = war;
        this.givenBy = castle.getPlaceName();
        this.givenByAggressor = war.isAggressor(castle);
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
        return new CommandOutpostDailyAction(model, war, givenByAggressor);
    }

    @Override
    public boolean isFailed(Model model) {
        return false;
    }

    @Override
    public boolean givesDailyAction(Model model) {
        return true;
    }

    @Override
    public boolean isCompleted() {
        return false;
    }

    private static class BattleJournalEntry implements JournalEntry {
        private final Point position;

        public BattleJournalEntry(Model model, Point position) {
            this.position = position;
        }

        @Override
        public String getName() {
            return "Battle of...";
        }

        @Override
        public String getText() {
            return "Talk to the field general at the command outpost about helping out in the upcoming battle against...";
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
            return position;
        }
    }
}
