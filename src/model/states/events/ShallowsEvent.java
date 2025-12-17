package model.states.events;

import model.Model;

public class ShallowsEvent extends RiverEvent {
    public ShallowsEvent(Model model) {
        super(model, false);
    }

    @Override
    protected void doRiverEvent(Model model) {
        showEventCard("Shallows", "The river is shallow and there is no undertow. The party can wade across.");
        print("Press enter to continue.");
        waitForReturn();
    }

    @Override
    public boolean eventPreventsCrossing(Model model) {
        return false;
    }
}
