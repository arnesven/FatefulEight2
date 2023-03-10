package model.states;

import model.Model;

public class NoLodgingState extends EveningState {
    private final boolean freeRations;

    public NoLodgingState(Model model, boolean freeRations) {
        super(model);
        this.freeRations = freeRations;
    }

    @Override
    public GameState run(Model model) {
        setCurrentTerrainSubview(model);
        print("Evening has come. ");
        model.getTutorial().evening(model);
        checkForQuest(model);
        if (freeRations) {
            println("The party has received rations for free.");
            model.getParty().consumeRations(true);
        } else {
            notLodging(model);
        }
        stepToNextDay(model);
        return nextState(model);
    }
}
