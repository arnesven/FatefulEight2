package model.states.events;

import model.Model;

public class RaftOnRiverEvent extends RiverEvent {

    public RaftOnRiverEvent(Model model) {
        super(model, true);
    }

    @Override
    public boolean eventPreventsCrossing(Model model) {
        return true;
    }

    @Override
    protected void doRiverEvent(Model model) {
        RaftEvent innerEvent = new RaftEvent(model, "cross the river");
        innerEvent.doEvent(model);
    }

}
