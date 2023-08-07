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
        if (model.getParty().hasHorses()) {
            println("Your horses can not accompany you into the cave, you will have to abandon them if you want to explore the caves.");
        }
        print("Enter the caves? (Y/N) ");
        if (yesNoInput()) {
            model.getParty().getHorseHandler().abandonHorses(model);
            model.enterCaveSystem();
        }
    }
}
