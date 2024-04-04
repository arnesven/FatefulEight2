package model.tasks;

import model.Model;
import model.actions.DailyAction;
import model.items.MysteriousMap;
import model.journal.JournalEntry;
import model.states.events.DigForTreasureEvent;

import java.awt.*;

public class TreasureHuntTask extends DestinationTask {
    private final MysteriousMap map;
    private boolean completed;

    public TreasureHuntTask(MysteriousMap mysteriousMap) {
        super(mysteriousMap.getLocation(), "asdf?? TODO");
        this.map = mysteriousMap;
        this.completed = false;
    }

    @Override
    public JournalEntry getJournalEntry(Model model) {
        return new TreasureHuntJournalEntry();
    }

    @Override
    public JournalEntry getFailedJournalEntry(Model model) {
        return null;
    }

    @Override
    public DailyAction getDailyAction(Model model) {
        return new DailyAction("Dig for Treasure", new DigForTreasureEvent(model));
    }

    @Override
    public boolean isFailed(Model model) {
        return !model.getParty().getInventory().getBooks().contains(map);
    }

    @Override
    public boolean givesDailyAction(Model model) {
        return !isFailed(model) && !completed;
    }

    public void complete(Model model) {
        completed = true;
        model.getParty().getInventory().remove(map);
        model.getLog().addAnimated("You discarded the treasure map.\n");
    }

    private class TreasureHuntJournalEntry implements JournalEntry {
        @Override
        public String getName() {
            return "A Mysterious Map";
        }

        @Override
        public String getText() {
            if (completed) {
                return "You used the strange map to find the treasure.\n\nCompleted";
            }
            return "You have come upon a strange map. You suspect that it may lead to treasure, " +
                    "but it may just be a wild goose chase.\n\n" +
                    "You can read the map from the Inventory Menu.";
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
            if (completed) {
                return map.getLocation();
            }
            return null;
        }
    }
}
