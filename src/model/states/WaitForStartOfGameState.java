package model.states;

import control.GameExitedException;
import model.Model;

public class WaitForStartOfGameState extends GameState {
    public WaitForStartOfGameState(Model model) {
        super(model);
    }

    @Override
    public GameState run(Model model) {
        System.out.print("Waiting for game to start");
        while (!model.gameStarted()) {
            System.out.print(".");
            delay(1000);
        }
        System.out.println("");
        return null;
    }
}
