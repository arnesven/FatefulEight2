package model.states.dailyaction;

import model.Model;
import model.states.DailyActionState;
import model.states.EveningState;
import model.states.GameState;

public class BuyRationsState extends GameState {
    public BuyRationsState(Model model) {
        super(model);
    }

    @Override
    public GameState run(Model model) {
        EveningState.buyRations(model, this);
        return new DailyActionState(model);
    }
}
