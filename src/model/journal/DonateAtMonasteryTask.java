package model.journal;

import model.Model;
import model.map.WorldBuilder;
import model.states.events.VisitMonasteryEvent;

import java.awt.*;

public class DonateAtMonasteryTask implements JournalEntry {

    private final int amount;

    public DonateAtMonasteryTask(Model model) {
        this.amount = VisitMonasteryEvent.getDonatedAmount(model);
    }

    @Override
    public String getName() {
        return "Donate to Monastery";
    }

    @Override
    public String getText() {
        return "The Sixth Monks on the Isle of Faith are asking for donations to restore the Monastery.\n\n" +
                "For every " + VisitMonasteryEvent.GOLD_PER_REP + " gold you donate your party will receive one Reputation.\n\n" +
                "You have donated " + amount + " gold so far.";
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
        return WorldBuilder.MONASTERY_POSITION;
    }
}
