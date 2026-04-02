package model.states.events;

import model.Model;
import model.actions.DailyAction;
import model.characters.GameCharacter;
import model.characters.appearance.FaceDetail;
import model.characters.appearance.SnakeDetail;
import model.journal.JournalEntry;
import model.map.HexLocation;
import model.map.UrbanLocation;
import model.tasks.DestinationTask;

import java.awt.*;

public class FindGrumbledookTask extends DestinationTask {
    public static final String TASK_NAME = "Invisible!";
    private final String destinationName;
    private final GameCharacter victim;
    private boolean met = false;
    private SnakeDetail snake;
    private boolean completed = false;

    public FindGrumbledookTask(Point position, UrbanLocation townOrCastle, GameCharacter victim) {
        super(position, "");
        this.destinationName = ((HexLocation)townOrCastle).getName();
        this.victim = victim;
        snake = new SnakeDetail();
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
                if (isComplete()) {
                    return "You cured " + victim.getName() + "'s invisibility with help from Grumbledook the great.\n\nCompleted";
                }


                if (isFailed()) {
                    return victim.getName() + " has turned invisible from eating a strange fruit. " +
                            "You were to find the enchanter Grumbledook, but " + victim.getName() + " is no longer in your party.";
                }

                if (met) {
                    return victim.getName() + " must wear a magical sneak around the " +
                            "neck to suppress the permanent invisibility. However Grumbledook can " +
                            "perform a ritual which may allow " + victim.getFirstName() +
                            " to remove the snake and stay visibile. To perform the ritual, however, Grumbledook " +
                            "needs an item made from bone.";
                }

                return victim.getName() + " has turned invisible from eating a strange fruit. " +
                        "Find the enchanter Grumbledook in " + destinationName + ", who supposedly is an " +
                        "expert at these sort of things.";
            }

            @Override
            public boolean isComplete() {
                return FindGrumbledookTask.this.isCompleted();
            }

            @Override
            public boolean isFailed() {
                return FindGrumbledookTask.this.isFailed(model);
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
        return !model.getParty().getPartyMembers().contains(victim);
    }

    @Override
    public boolean givesDailyAction(Model model) {
        HexLocation loc = model.getWorld().getLocationByName(destinationName);
        return !isFailed(model) &&
                model.partyIsInOverworldPosition(model.getWorld().getPositionForLocation(loc));
    }

    @Override
    public boolean isCompleted() {
        return completed;
    }

    public void setGrumbledookMet(boolean b) {
        this.met = b;
    }

    public boolean isGrumbledookMet() {
        return met;
    }

    public FaceDetail getSnake() {
        return snake;
    }

    public void setCompleted(boolean b) {
        this.completed = b;
    }
}
