package model.states;

import model.Model;

public class NoEventState extends DailyEventState {
    public NoEventState(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("No event today.");
    }
}
