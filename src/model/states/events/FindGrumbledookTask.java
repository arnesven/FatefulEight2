package model.states.events;

import model.Model;
import model.actions.DailyAction;
import model.characters.GameCharacter;
import model.journal.JournalEntry;
import model.map.HexLocation;
import model.map.UrbanLocation;
import model.tasks.DestinationTask;

import java.awt.*;

public class FindGrumbledookTask extends DestinationTask {
    public static final String TASK_NAME = "Invisible!";
    private final String destinationName;
    private final GameCharacter victim;

    public FindGrumbledookTask(Point position, UrbanLocation townOrCastle, GameCharacter victim) {
        super(position, "");
        this.destinationName = ((HexLocation)townOrCastle).getName();
        this.victim = victim;
    }

    @Override
    public JournalEntry getJournalEntry(Model model) {
        return new JournalEntry() {
            @Override
            public String getName() {
                return TASK_NAME;
            }

            @Override
            public String getText() {
                return victim.getName() + " has turned invisible from eating a strange fruit. " +
                        "Find the enchanter Grumbledook in " + destinationName + ", who supposedly is an " +
                        "expert at these sort of things.";
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
                HexLocation loc = model.getWorld().getLocationByName(destinationName);
                return model.getWorld().getPositionForLocation(loc);
            }
        };
    }

    @Override
    public JournalEntry getFailedJournalEntry(Model model) {
        return getJournalEntry(model);
    }

    @Override
    public DailyAction getDailyAction(Model model) {
        return new DailyAction("Seek Grumbledook", new MeetGrumbledookEvent(model, victim, this));
    }

    @Override
    public boolean isFailed(Model model) {
        return false;
    }

    @Override
    public boolean givesDailyAction(Model model) {
        HexLocation loc = model.getWorld().getLocationByName(destinationName);
        return model.partyIsInOverworldPosition(model.getWorld().getPositionForLocation(loc));
    }

    @Override
    public boolean isCompleted() {
        return false;
    }
}
