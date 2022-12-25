package model.actions;

import model.Model;
import model.states.StayInHexState;

public class StayInHexAction extends DailyAction {
    public StayInHexAction(Model model) {
        super("Stay in Hex", new StayInHexState(model));
    }

    @Override
    public char getShortKey() {
        return 'X';
    }
}
