package model.states.events;

import model.Model;
import model.states.DailyEventState;

import java.util.List;

public class CaveEvent extends DailyEventState {
    public CaveEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        model.getParty().randomPartyMemberSay(model, List.of("Hey, there's a cave here!",
                "Looks like a system of caves.", "A cave. Should we explore it?"));
        print("Enter the caves? (Y/N) ");
        if (yesNoInput()) {
            model.enterCaveSystem();
        }
    }
}
