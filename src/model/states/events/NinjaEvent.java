package model.states.events;

import model.Model;
import model.classes.Classes;
import model.states.DailyEventState;

public class NinjaEvent extends DailyEventState {
    public NinjaEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        new ChangeClassEvent(model, Classes.NINJA).areYouInterested(model);
    }
}
