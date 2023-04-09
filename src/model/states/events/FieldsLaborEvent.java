package model.states.events;

import model.Model;
import model.states.DailyEventState;

import java.util.List;

public abstract class FieldsLaborEvent extends DailyEventState {
    private boolean freeRations;

    public FieldsLaborEvent(Model model) {
        super(model);
        freeRations = false;
    }

    protected void failure(Model model, String sitch) {
        println("The party just made a mess of things " + sitch + ". The farmer is appalled by " +
                "the party's inability to do even a simple job and angrily asks you to be " +
                "on your way.");
        model.getParty().randomPartyMemberSay(model,
                List.of("I'm not one for manual labor anyway.",
                        "Jeez, we were only trying to help...",
                        "But I got my clothes dirty!#",
                        "Come on people, let's get out of here.",
                        "Well, this was a waste of time.",
                        "Wow, he seemed pretty angry.",
                        "I'm glad I wasn't born a farmer."));
    }

    protected void success(Model model) {
        println("The farmer thanks the party for the help and offers a small bag of coins (20 gold).");
        model.getParty().addToGold(20);
        new GuestEvent(model).doEvent(model);
        freeRations = true;
    }

    @Override
    public boolean isFreeRations() {
        return freeRations;
    }
}
