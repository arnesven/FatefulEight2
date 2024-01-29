package model.states;

import model.Model;

import java.util.List;

public class NoLodgingState extends EveningState {
    private final boolean freeRations;

    public NoLodgingState(Model model, boolean freeRations) {
        super(model);
        this.freeRations = freeRations;
    }

    @Override
    protected void locationSpecificEvening(Model model) {
        if (model.getSpellHandler().creatureComfortsCastToday(model)) {
            println("The party has received food and lodging.");
            model.getParty().lodging(0);
        } else if (freeRations) {
            println("The party has received rations for free.");
            model.getParty().consumeRations(true);
            sayThanks(model);
        } else {
            notLodging(model);
        }
    }

    private void sayThanks(Model model) {
        model.getParty().randomPartyMemberSay(model, List.of("Much obliged", "How generous!",
                "They say there's nothing like a free lunch. But perhaps a dinner?",
                "Lucky us!", "Free food! Hurray!", "I really appreciate this."));
    }
}
