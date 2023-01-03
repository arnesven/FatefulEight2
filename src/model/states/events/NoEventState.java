package model.states.events;

import model.Model;
import model.states.DailyEventState;

public class NoEventState extends DailyEventState {
    public NoEventState(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("No event today.");
    }
}
