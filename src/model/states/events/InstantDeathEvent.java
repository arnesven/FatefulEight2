package model.states.events;

import model.Model;
import model.states.DailyEventState;
import util.MyRandom;

/**
 * This event was written for testing of Evening Grief Handling.
 */
public class InstantDeathEvent extends DailyEventState {
    public InstantDeathEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        int toKill = 1;
        for (int i = 0; i < toKill; i++) {
            characterDies(model, this, MyRandom.sample(model.getParty().getPartyMembers()),
                    " has been killed by instant death!", false);
        }
    }
}
