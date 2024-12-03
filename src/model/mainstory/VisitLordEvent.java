package model.mainstory;

import model.Model;
import model.states.DailyEventState;

public abstract class VisitLordEvent extends DailyEventState {
    public VisitLordEvent(Model model) {
        super(model);
    }

    public boolean returnEveningState() { return false; }

}
