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
                System.out.println("Waiting for game to start...");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
