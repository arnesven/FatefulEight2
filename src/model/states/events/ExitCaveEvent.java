package model.states.events;

import model.Model;
import model.states.DailyEventState;

import java.util.List;

public class ExitCaveEvent extends DailyEventState {
    public ExitCaveEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        model.getParty().randomPartyMemberSay(model, List.of("There's a way out here!",
                "Daylight! We can get out here!", "An exit, finally.", "Looks like there's an exit here."));
        print("Exit the caves? (Y/N) ");
        if (yesNoInput()) {
            model.exitCaveSystem();
        }
    }
}
