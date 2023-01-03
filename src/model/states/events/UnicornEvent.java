package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.states.DailyEventState;

import java.util.List;

public class UnicornEvent extends DailyEventState {
    public UnicornEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("A shimmering horse-like creature approaches the party. " +
                "A long spiraling horn protrudes from its forehead. It " +
                "seems perfectly tame and gladly lets the party members " +
                "stroke its mane.");
        model.getParty().randomPartyMemberSay(model, List.of("It's beautiful!3", "Magnificent creature...",
                "Hey there pal...3", "Magical...", "I feel like I'm dreaming."));
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            gc.addToSP(100);
        }
        model.getParty().randomPartyMemberSay(model, List.of("I feel so refreshed.3",
                "Weird. It's like I've woken up from a long troubled sleep.",
                "I feel so good.3"));
    }
}
