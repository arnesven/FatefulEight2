package model.states.events;

import model.Model;

import java.util.List;

public class NoRiverCrossingEvent extends RiverEvent {
    public NoRiverCrossingEvent(Model model) {
        super(model);
    }

    @Override
    public boolean eventPreventsCrossing(Model model) {
        return true;
    }

    @Override
    protected void doEvent(Model model) {
        println("The river is deep, and the current is too strong to ford.");
        model.getParty().randomPartyMemberSay(model, List.of("We can't cross here.", "I'm actually not a very good swimmer.",
                "It looks cold...", "We should try to find somewhere else to cross.", "That current will sweep us away.",
                "Looks dangerous!"));
        print("The party cannot cross the river today. Press enter to continue.");
        waitForReturn();
    }
}
