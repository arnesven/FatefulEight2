package model.states;

import model.Model;

public class SaveGameState extends GameState {
    public SaveGameState(Model model) {
        super(model);
    }

    @Override
    public GameState run(Model model) {
        println("Saving game...");
        model.saveToFile();
        return new DailyActionState(model);
    }
}
