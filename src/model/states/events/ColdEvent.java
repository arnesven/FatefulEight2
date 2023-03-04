package model.states.events;

import model.Model;
import model.states.DailyEventState;

import java.util.List;

public class ColdEvent extends DailyEventState {
    public ColdEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        model.getParty().randomPartyMemberSay(model,
                List.of("Brrr... I'm so cold!", "My pants, they're like ice.",
                        "I'm shaking so much that I feel like I'm dancing.",
                        "Can we please go someplace warmer?"));
        println("The frigid cold has the party members shivering to their " +
                "teeth. They must consume more food to stay warm.");
        model.getParty().addToFood(-model.getParty().size());
    }
}
