package model.states;

import model.Model;

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
        } else {
            notLodging(model);
        }
    }
}
