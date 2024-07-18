package model.states;

import model.Model;
import view.LogView;
import view.SelectSaveSlotMenu;

public class SaveGameState extends GameState {
    public SaveGameState(Model model) {
        super(model);
    }

    @Override
    public GameState run(Model model) {
        model.transitionToDialog(new SelectSaveSlotMenu(model.getView(), false));
        println(LogView.GRAY_COLOR + "Saving game." + LogView.DEFAULT_COLOR);
        return new DailyActionState(model);
    }
}
