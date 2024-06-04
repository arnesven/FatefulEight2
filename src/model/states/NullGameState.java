package model.states;

import model.Model;

public class NullGameState extends GameState {
    public NullGameState() {
        super(null);
    }

    @Override
    public GameState run(Model model) {
        return null;
    }
}
