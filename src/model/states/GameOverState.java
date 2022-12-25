package model.states;

import model.Model;

public class GameOverState extends GameState {
    public GameOverState(Model model) {
        super(model);
    }

    @Override
    public GameState run(Model model) {
       return this;
    }
}
