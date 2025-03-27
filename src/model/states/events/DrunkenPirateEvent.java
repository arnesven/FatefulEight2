package model.states.events;

import model.Model;
import model.classes.Classes;
import model.states.DailyEventState;

public class DrunkenPirateEvent extends DailyEventState {
    public DrunkenPirateEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        showRandomPortrait(model, Classes.PIRATE_CAPTAIN, "Pirate Captain");
        waitForReturn();
    }
}
