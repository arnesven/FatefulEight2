package model.states.dailyaction;

import model.Model;
import model.states.EveningState;
import model.states.GameState;

public class LodgingState extends EveningState {
    private final boolean freeLodging;

    public LodgingState(Model model, boolean freeLodging) {
        super(model);
        this.freeLodging = freeLodging;
    }

    @Override
    public GameState run(Model model) {
        setCurrentTerrainSubview(model);
        print("Evening has come. ");
        checkForQuest(model);
        if (freeLodging) {
            println("The party receives food and lodging for free.");
            model.getParty().lodging(0);
        } else {
            model.getParty().lodging(EveningState.lodgingCost(model));
        }
        model.incrementDay();
        return nextState(model);
    }
}
