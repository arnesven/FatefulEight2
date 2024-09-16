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
            try {
                System.out.print(".");
                Thread.sleep(1000);
                if (model.gameExited()) {
                    throw new GameExitedException();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println("");
        return null;
    }
}
