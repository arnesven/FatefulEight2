package model.states.events;

import model.Model;
import model.states.DailyEventState;

public class SilentNoEventState extends DailyEventState {
    public SilentNoEventState(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) { }
}
