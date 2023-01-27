package model.states;

import model.Model;
import sound.SoundEffects;

public class SaveGameState extends GameState {
    public SaveGameState(Model model) {
        super(model);
    }

    @Override
    public GameState run(Model model) {
        println("Saving game in slot 1...");
        model.saveToFile("slot1");
        SoundEffects.gameSaved();
        return new DailyActionState(model);
    }
}
