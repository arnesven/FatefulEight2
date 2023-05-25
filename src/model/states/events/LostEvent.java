package model.states.events;

import model.Model;
import model.states.DailyEventState;
import model.states.EveningState;
import model.states.GameOverState;

import java.util.List;

public class LostEvent extends DailyEventState {
    private DailyEventState innerEvent;

    public LostEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        if (model.getParty().size() > 1) {
            model.getParty().randomPartyMemberSay(model, List.of("This place looks familiar..."));
            model.getParty().randomPartyMemberSay(model, List.of("It's because we've been here before."));
            println("The party has lost its way in the wilderness and has made no progress today.");
        } else {
            println("You have lost your way in the wilderness and have made no progress today.");
        }
        new EveningState(model, false, false).run(model);
        if (model.getParty().isWipedOut()) {
            return;
        }
        innerEvent = model.getCurrentHex().generateEvent(model);
        innerEvent.run(model);
    }

    @Override
    public boolean haveFledCombat() {
        return innerEvent.haveFledCombat();
    }
}
