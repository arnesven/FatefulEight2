package model.states.events;

import model.Model;
import model.states.DailyEventState;

public class AbandonedShackEvent extends SalvageEvent {
    public AbandonedShackEvent(Model model) {
        super(model, "n abandoned shack", 10);
    }
}
