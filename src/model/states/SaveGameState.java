package model.states;

import model.Model;
import sound.SoundEffects;
import view.SelectSaveSlotMenu;

public class SaveGameState extends GameState {
    public SaveGameState(Model model) {
        super(model);
    }

    @Override
    public GameState run(Model model) {
        model.transitionToDialog(new SelectSaveSlotMenu(model.getView(), false));
        println("Saving game...");
        return new DailyActionState(model);
    }
}
