package model.states;

import model.Model;

public class WaitForStartOfGameState extends GameState {
    public WaitForStartOfGameState(Model model) {
        super(model);
    }

    @Override
    public GameState run(Model model) {
        while (!model.gameStarted()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
