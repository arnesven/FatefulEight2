package model.states;

import model.Model;

public class WaitForStartOfGameState extends GameState {
    public WaitForStartOfGameState(Model model) {
        super(model);
    }

    @Override
    public GameState run(Model model) {
        System.out.print("Waiting for game to start");
        while (!model.gameStarted()) {
            try {
                System.out.print(".");
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("");
        return null;
    }
}
