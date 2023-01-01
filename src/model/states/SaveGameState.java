package model.states;

import model.Model;

public class SaveGameState extends GameState {
    public SaveGameState(Model model) {
        super(model);
    }

    @Override
    public GameState run(Model model) {
        println("Saving game in slot 1...");
        model.saveToFile("slot1");
        return new DailyActionState(model);
    }
}
