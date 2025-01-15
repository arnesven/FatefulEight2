package model.tasks;

import model.Model;
import model.actions.DailyAction;
import model.actions.Loan;
import model.journal.JournalEntry;
import model.states.GameState;
import model.states.events.CarryOutAssassinationEvent;
import util.MyLists;

import java.awt.*;

public class AssassinationDestinationTask extends DestinationTask {

    private final WritOfExecution writ;
    private boolean completed = false;
    private boolean failed = false;

    public AssassinationDestinationTask(WritOfExecution writ) {
        super(writ.getPosition(), writ.getDestinationLongDescription());
        this.writ = writ;
    }

    @Override
    public JournalEntry getJournalEntry(Model model) {
        return new AssassinationTaskJournalEntry(false, writ.getDaysLeft(model));
    }

    @Override
    public JournalEntry getFailedJournalEntry(Model model) {
        return new AssassinationTaskJournalEntry(true, 0);
    }

    @Override
    public DailyAction getDailyAction(Model model) {
        return new FindWritTargetDailyAction(model, this);
    }

    @Override
    public boolean isFailed(Model model) {
        return failed || writ.hasExpired(model);
    }

    @Override
    public boolean givesDailyAction(Model model) {
        return !isFailed(model) && !completed && model.partyIsInOverworldPosition(getPosition());
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }

    public void setCompleted(boolean b) {
        completed = b;
    }

    public void setFailed(boolean b) {
        this.failed = b;
    }

    @Override
    public void runStartOfDayHook(Model model) {
        if (isCompleted() || failed) {
            return;
        }
        if (writ.hasExpired(model)) {
            setFailed(true);
            int prevLoan = 0;
            if (model.getParty().getLoan() != null) {
                prevLoan = model.getParty().getLoan().getAmount();
            }
            int loanSum = prevLoan + WritOfExecution.getPayment();
            model.getLog().addAnimated("You have failed to carry out the assassination of " +
                    writ.getName() + ". Your debt of " + WritOfExecution.getPayment() + " gold must be repaid " +
                    "to the Brotherhood within " + Loan.REPAY_WITHIN_DAYS + " days.\n");
            model.getTutorial().loans(model);
            model.getParty().setLoan(new Loan(loanSum, model.getDay()));
            JournalEntry.printJournalUpdateMessage(model);
        }
    }

    public WritOfExecution getWrit() {
        return writ;
    }

    public static boolean runLodgingNodeHook(Model model, GameState state) {
        AssassinationDestinationTask task = getInfoBrokerTask(model);
        if (task == null) {
            return false;
        }
        state.print("Do you wish to search the tavern for " + task.getWrit().getName() + "? (Y/N) ");
        return state.yesNoInput();
    }

    public static AssassinationDestinationTask getInfoBrokerTask(Model model) {
        return (AssassinationDestinationTask) MyLists.find(model.getParty().getDestinationTasks(),
                dt -> model.partyIsInOverworldPosition(dt.getPosition()) &&
                        dt instanceof AssassinationDestinationTask &&
                        !dt.isCompleted() &&
                        !dt.isFailed(model) &&
                        ((AssassinationDestinationTask) dt).getWrit().isInfoBrokerFound());
    }

    private static class FindWritTargetDailyAction extends DailyAction {
        public FindWritTargetDailyAction(Model model, AssassinationDestinationTask task) {
            super("Find writ target", new CarryOutAssassinationEvent(model, task));
        }
    }

    private class AssassinationTaskJournalEntry implements JournalEntry {

        private final boolean failed;
        private final int daysLeft;

        public AssassinationTaskJournalEntry(boolean failed, int daysLeft) {
            this.failed = failed;
            this.daysLeft = daysLeft;
        }

        @Override
        public String getName() {
            return "Kill " + writ.getName();
        }

        @Override
        public String getText() {
            if (isFailed()) {
                return "You accepted a Writ of Execution for " + writ.getName() + " but you failed " +
                        "to carry out the assassination in time.";
            }
            if (isComplete()) {
                return "You accepted a Writ of Execution from the Crimson Assassins for an assassination of " +
                        writ.getName() + ". You successfully carried out the assassination.\n\nCompleted";
            }
            String race = writ.getRace().getName().toLowerCase();
            String extra = "";
            if (writ.gotClue()) {
                extra = "You've learned that this " + race + " is a " +
                        writ.getOccupationDescription() + ".\n\n";
            }
            return "You've accepted a Writ of Execution from the Crimson Assassins. " +
                    "It directs you to take the life of a " + race + ", " +
                    writ.getName() + ", who can be found " +
                    writ.getDestinationLongDescription() + ".\n\n" +
                    extra +
                    "Days left: " + daysLeft;
        }

        @Override
        public boolean isComplete() {
            return AssassinationDestinationTask.this.isCompleted();
        }

        @Override
        public boolean isFailed() {
            return failed;
        }

        @Override
        public boolean isTask() {
            return true;
        }

        @Override
        public Point getPosition(Model model) {
            if (failed || writ.hasExpired(model) || isComplete()) {
                return null;
            }
            return writ.getPosition();
        }
    }
}
