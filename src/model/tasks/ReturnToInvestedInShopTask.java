package model.tasks;

import model.Model;
import model.actions.DailyAction;
import model.journal.JournalEntry;
import model.map.TownLocation;
import model.map.UrbanLocation;
import model.states.events.InvestInShopEvent;

import java.awt.*;

public class ReturnToInvestedInShopTask extends DestinationTask {
    private final String townName;
    private final int investment;

    public ReturnToInvestedInShopTask(Point position, UrbanLocation location, int investment) {
        super(new Point(position), "");
        this.townName = location instanceof TownLocation ? ((TownLocation) location).getTownName() : location.getPlaceName();
        this.investment = investment;
    }

    @Override
    public JournalEntry getJournalEntry(Model model) {
        return new JournalEntry() {
            @Override
            public String getName() {
                return "Shop Investment: " + townName;
            }

            @Override
            public String getText() {
                return "You have invested " + investment + " gold in a shop in " + townName +
                        ". You should return soon to check in how the business is going.";
            }

            @Override
            public boolean isComplete() {
                return false; // Can't complete
            }

            @Override
            public boolean isFailed() {
                return false; // Can't fail
            }

            @Override
            public boolean isTask() {
                return true;
            }

            @Override
            public Point getPosition(Model model) {
                return ReturnToInvestedInShopTask.this.getPosition();
            }
        };
    }

    @Override
    public JournalEntry getFailedJournalEntry(Model model) {
        return null;
    }

    @Override
    public DailyAction getDailyAction(Model model) {
        return new DailyAction("Visit Investment", new InvestInShopEvent(model));
    }

    @Override
    public boolean isFailed(Model model) {
        return false;
    }

    @Override
    public boolean givesDailyAction(Model model) {
        return model.partyIsInOverworldPosition(getPosition());
    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}
