package model.states.events;

import model.Model;
import model.TimeOfDay;
import model.states.DailyEventState;
import model.states.GameState;

public abstract class NightTimeEvent extends DailyEventState {

    public NightTimeEvent(Model model) {
        super(model);
    }

    @Override
    protected void doStartOfEventHook(Model model) {
        model.setTimeOfDay(TimeOfDay.NIGHT);
    }

    @Override
    protected GameState doEndOfEventHook(Model model) {
        return null; // Should not be used.
    }
}
