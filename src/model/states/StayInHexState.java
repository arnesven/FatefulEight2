package model.states;

import model.Model;

public class StayInHexState extends GameState {
    public StayInHexState(Model model) {
        super(model);
    }

    @Override
    public GameState run(Model model) {
        setCurrentTerrainSubview(model);
        return model.getCurrentHex().generateEvent(model).run(model);
    }
}
