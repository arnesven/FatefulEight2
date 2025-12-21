package model.tasks;

import model.Model;
import model.actions.DailyAction;
import model.journal.JournalEntry;
import model.states.events.GelatinousBlobEvent;
import util.MyStrings;
import view.MyColors;

import java.awt.*;
import java.util.HashMap;

public class GelatinousBlobTask extends DestinationTask {
    private static final int NUMBER_NEEDED_OF_EACH = 8;
    private final HashMap<MyColors, Integer> countMap;

    public GelatinousBlobTask(Point position) {
        super(position, "Perform the task and turn in at a League of Mages Guild Hall");
        this.countMap = new HashMap<>();
        for (MyColors color : GelatinousBlobEvent.getBlobColors()) {
            countMap.put(color, 0);
        }
    }

    @Override
    public boolean drawTaskOnMap(Model model) {
        return true;
    }

    @Override
    public JournalEntry getJournalEntry(Model model) {
        return new GelatinousBlobJournalEntry();
    }

    @Override
    public JournalEntry getFailedJournalEntry(Model model) {
        return new GelatinousBlobJournalEntry();
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
        for (MyColors color : countMap.keySet()) {
            if (countMap.get(color) < NUMBER_NEEDED_OF_EACH) {
                return false;
            }
        }
        return true;
    }

    private class GelatinousBlobJournalEntry implements JournalEntry {
        @Override
        public String getName() {
            return "Kill Gelatinous Blobs";
        }

        @Override
        public String getText() {
            StringBuilder bldr = new StringBuilder("To verify the research notes on Gelatinous Blobs, you need to kill a number of them.\n\n");
            for (MyColors color : countMap.keySet()) {
                bldr.append(String.format("%-15s%d/%d\n", MyStrings.capitalize(color.name().toLowerCase()) + " Blobs:", countMap.get(color), NUMBER_NEEDED_OF_EACH));
            }
            return bldr.toString();
        }

        @Override
        public boolean isComplete() {
            return GelatinousBlobTask.this.isCompleted();
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
            return GelatinousBlobTask.this.getPosition();
        }
    }
}
