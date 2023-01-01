package model.states.events;

import model.Model;

public class ShallowsEvent extends RiverEvent {
    public ShallowsEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("The river is shallow and there is no undertow. The party can wade across.");
        print("Press enter to continue.");
        waitForReturn();
    }

    @Override
    public boolean eventPreventsCrossing(Model model) {
        return false;
    }
}
