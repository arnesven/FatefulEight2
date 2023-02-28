package model.tasks;

import model.Model;
import model.states.DailyEventState;

public abstract class SummonTask extends DailyEventState {
    public SummonTask(Model model) {
        super(model);
    }

    public void doTask(Model model) {
        doEvent(model);
    }
}
