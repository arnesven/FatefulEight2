package model.tasks;

import model.GameStatistics;
import model.Model;
import model.actions.DailyAction;
import model.characters.GameCharacter;
import model.journal.JournalEntry;
import model.map.UrbanLocation;
import model.states.DailyEventState;
import util.MyLists;
import util.MyStrings;

import java.awt.*;

public abstract class WorkbenchTask extends DestinationTask {
    private final String verb;

    public WorkbenchTask(String verb) {
        super(null, "Use the workbench in a town or castle to " + verb + " an item.");
        this.verb = verb;
    }

    public static DailyEventState makeFirstTimeAtCraftingBenchEvent(Model model) {
        if (model.getCurrentHex().getLocation() instanceof UrbanLocation &&
            !MyLists.any(model.getParty().getDestinationTasks(), dt -> dt instanceof WorkbenchTask)) {
            return new CraftItemTaskEvent(model);
        }
        return null;
    }

    @Override
    public JournalEntry getJournalEntry(Model model) {
        return new CraftItemJorunalEntry();
    }

    @Override
    public JournalEntry getFailedJournalEntry(Model model) {
        return new CraftItemJorunalEntry();
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
    public abstract boolean isCompleted();

    private static class CraftItemTaskEvent extends DailyEventState {
        public CraftItemTaskEvent(Model model) {
            super(model);
        }

        @Override
        protected void doEvent(Model model) {
            leaderSay("Hey, there's a crafting bench here.");
            GameCharacter other = model.getParty().getLeader();
            if (model.getParty().size() > 1) {
                other = model.getParty().getRandomPartyMember(model.getParty().getLeader());
            }
            partyMemberSay(other, "If " + iOrWe() + " find some materials we could make or upgrade some gear.");
            leaderSay("Looks like somebody has left some useful stuff here. Maybe " + iOrWe() + " can make something out of it?");
            println("You gain 3 materials.");
            model.getParty().getInventory().addToMaterials(3);
            model.getParty().addDestinationTask(new CraftItemTask());
            model.getParty().addDestinationTask(new UpgradeItemTask());
            JournalEntry.printJournalUpdateMessage(model);
            model.getLog().waitForAnimationToFinish();
        }
    }

    private static class UpgradeItemTask extends WorkbenchTask {
        public UpgradeItemTask() {
            super("upgrade");
        }

        @Override
        public boolean isCompleted() {
            return GameStatistics.getItemsUpgraded() > 0;
        }
    }

    private static class CraftItemTask extends WorkbenchTask {
        public CraftItemTask() {
            super("craft");
        }

        @Override
        public boolean isCompleted() {
            return GameStatistics.getItemsCrafted() > 0;
        }
    }

    private class CraftItemJorunalEntry implements JournalEntry {
        @Override
        public String getName() {
            return MyStrings.capitalize(WorkbenchTask.this.verb) + " an item";
        }

        @Override
        public String getText() {
            return WorkbenchTask.this.getDestinationDescription() +
                    (isComplete() ? "\n\nCompleted" : "");
        }

        @Override
        public boolean isComplete() {
            return WorkbenchTask.this.isCompleted();
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
            return null;
        }
    }
}
