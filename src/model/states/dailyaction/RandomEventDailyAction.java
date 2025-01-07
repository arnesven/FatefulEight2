package model.states.dailyaction;

import model.Model;
import model.actions.DailyAction;
import model.states.StayInHexState;

public class RandomEventDailyAction extends DailyAction {
    public RandomEventDailyAction(Model model) {
        super("Random event", new StayInHexState(model));
    }
}
