package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Classes;
import model.states.DailyEventState;

public class CaptainEvent extends DailyEventState {
    public CaptainEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("The party meets the master-at-arms of the castle. He seems to be in a good mood and happily shows " +
                "you a few tricks with sword, spear and axe.");
        println("Each party member gains 10 experience.");
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            model.getParty().giveXP(model, gc, 10);
        }
        print("The master-at-arms also offers to instruct you in the ways of being a Captain, ");
        ChangeClassEvent change = new ChangeClassEvent(model, Classes.CAP);
        change.areYouInterested(model);
    }
}
