package model.states.events;

import model.Model;
import model.classes.Classes;
import model.states.DailyEventState;

public class AmazonEvent extends DailyEventState {
    public AmazonEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("A large, handsome and scantily clad person stands " +
                "before the party. From the way that they are talking you " +
                "understand that this is a native warrior of great skill.");
        ChangeClassEvent change = new ChangeClassEvent(model, Classes.AMZ);
        print("The native warrior offers to instruct you in the ways of being an Amazon, ");
        change.areYouInterested(model);
    }
}
