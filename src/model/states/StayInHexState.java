package model.states;

import model.Model;

public class StayInHexState extends GameState {
    public StayInHexState(Model model) {
        super(model);
    }

    @Override
    public GameState run(Model model) {
        return model.getCurrentHex().generateEvent(model).run(model);
    }
}
