package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.states.DailyEventState;

import java.util.List;

public class FaeriesEvent extends DailyEventState {
    public FaeriesEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("Exhausted after a day of travelling through the rough " +
                "the party stumbles into a clearing. As you sit down to " +
                "rest you notice small glowing orbs all around you. ");
        model.getLog().waitForAnimationToFinish();
        model.getParty().randomPartyMemberSay(model, List.of("Lightning bugs?"));
        model.getLog().waitForAnimationToFinish();
        model.getParty().randomPartyMemberSay(model, List.of("No, it's something else..."));
        model.getLog().waitForAnimationToFinish();
        model.getParty().randomPartyMemberSay(model, List.of("There are voices, as if children are laughing and singing."));
        model.getLog().waitForAnimationToFinish();
        model.getParty().randomPartyMemberSay(model, List.of("Faeries!3"));
        println("Each party member regains 3 HP and 1 SP");
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            gc.addToHP(3);
            gc.addToSP(1);
        }
    }
}
