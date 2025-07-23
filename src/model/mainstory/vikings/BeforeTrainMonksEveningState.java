package model.mainstory.vikings;

import model.Model;
import model.combat.CombatAdvantage;
import model.states.EveningState;
import model.states.GameState;

public class BeforeTrainMonksEveningState extends EveningState {
    public BeforeTrainMonksEveningState(Model model) {
        super(model, true, true, false);
    }

    @Override
    protected GameState nextState(Model model) {
        return new TrainMonksAtMonastaryEvent(model);
    }
}
