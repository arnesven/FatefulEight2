package model.actions;

import model.Model;
import model.states.SaveGameState;

public class SaveGameAction extends DailyAction {
    public SaveGameAction(Model model) {
        super("Save Game", new SaveGameState(model));
    }

    @Override
    public char getShortKey() {
        return 'V';
    }
}
