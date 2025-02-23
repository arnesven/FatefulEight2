package model.tasks;

import control.FatefulEight;
import model.Model;
import model.actions.DailyAction;
import model.characters.GameCharacter;
import model.journal.JournalEntry;
import model.states.GameState;
import model.states.events.RevisitBoyfriendGirlfriendEvent;

import java.awt.*;

public class BoyfriendGirlfriendDestinationTask extends DestinationTask {
    private final GameCharacter main;
    private final GameCharacter friend;
    private final String townName;
    private int lastDay;
    private int previousEventChoice;
    private int partyTalk;
    private boolean completed = false;
    private int step = 0;
    private boolean failed = false;

    public BoyfriendGirlfriendDestinationTask(Point position, String townName,
                                              GameCharacter mainCharacter, GameCharacter friendCharacter,
                                              int previousEventChoice, int lastDay) {
        super(position, "");
        this.townName = townName;
        this.main = mainCharacter;
        this.friend = friendCharacter;
        this.previousEventChoice = previousEventChoice;
        this.partyTalk = 0;
        if (previousEventChoice == 0) {
            partyTalk++;
        }
        this.lastDay = lastDay;
    }

    @Override
    public JournalEntry getJournalEntry(Model model) {
        return new BoyfriendGirlfriendJournalEntry();
    }

    @Override
    public JournalEntry getFailedJournalEntry(Model model) {
        return new FailedJournalEntry();
    }

    @Override
    public DailyAction getDailyAction(Model model) {
        return new DailyAction("Visit " + friend.getFirstName(),
                new RevisitBoyfriendGirlfriendEvent(model, main, friend, previousEventChoice, step));
    }

    @Override
    public boolean isFailed(Model model) {
        return failed;
    }

    @Override
    public boolean givesDailyAction(Model model) {
        if (!completed && !failed && model.partyIsInOverworldPosition(getPosition())) {
            if (step < 2) {
                return model.getDay() > lastDay + 2;
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }

    public void progress(int chosenTopic, int day) {
        this.step++;
        this.previousEventChoice = chosenTopic;
        if (previousEventChoice == 0) {
            partyTalk++;
        }
        this.lastDay = day;
    }

    public void setFailed(boolean b) {
        this.failed = b;
    }

    public void setCompleted(boolean b) {
        this.completed = b;
    }

    public int getPartyTalkCount() {
        return partyTalk;
    }

    private class BoyfriendGirlfriendJournalEntry implements JournalEntry {
        private final String boyOrGirlFriend;

        public BoyfriendGirlfriendJournalEntry() {
            this.boyOrGirlFriend = (friend.getGender() ? "girl" : "boy") + "friend";
        }

        @Override
        public String getName() {
            return main.getFirstName() + "'s " + boyOrGirlFriend;
        }

        @Override
        public String getText() {
            if (isComplete()) {
                return main.getName() + " had a romantic encounter with " + friend.getName() + ", in " + townName +
                        ". They are now happily together.\n\nCompleted";
            }
            String extra = "";
            if (FatefulEight.inDebugMode()) {
                extra = "\n\n" +
                        "Step: " + step + "\n" +
                        "Last day: " + lastDay + "\n" +
                        "Party talk: " + partyTalk + "\n" +
                        "Attitude: " + friend.getAttitude(main);
            }
            return main.getName() + " has a " + boyOrGirlFriend + ", " + friend.getName() + ", in " + townName +
                    ". You should visit " + GameState.himOrHer(friend.getGender()) + " again soon." + extra;
        }

        @Override
        public boolean isComplete() {
            return BoyfriendGirlfriendDestinationTask.this.isCompleted();
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
            return BoyfriendGirlfriendDestinationTask.this.getPosition();
        }
    }

    private class FailedJournalEntry implements JournalEntry {
        private final String boyOrGirlFriend;

        public FailedJournalEntry() {
            this.boyOrGirlFriend = (friend.getGender() ? "girl" : "boy") + "friend";
        }

        @Override
        public String getName() {
            return main.getFirstName() + "'s " + boyOrGirlFriend;
        }

        @Override
        public String getText() {
            return main.getName() + " had a romantic encounter with " + friend.getName() + ", in " + townName +
                    ". But the relationship never got very serious.";
        }

        @Override
        public boolean isComplete() {
            return false;
        }

        @Override
        public boolean isFailed() {
            return true;
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
