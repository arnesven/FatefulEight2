package model.states;

import model.Model;

public class GameOverState extends GameState {
    public GameOverState(Model model) {
        super(model);
    }

    @Override
    public GameState run(Model model) {
        model.setGameOver(true);
        model.setGameStarted(false);
        return new WaitForStartOfGameState(model);
    }
}
