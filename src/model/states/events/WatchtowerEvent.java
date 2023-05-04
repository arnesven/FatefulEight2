package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.states.DailyEventState;

import java.util.List;

public class WatchtowerEvent extends DailyEventState {
    public WatchtowerEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("Up on a hill, an ancient watchtower sits silently and " +
            "resolutely. The party climbs the stone steps to find it " +
            "completely abandoned. However, there are majestic " +
            "statues, murals with powerful imagery and the view is " +
            "spectacular. From here however, you can see all the way " +
            "to the ocean.");
        model.getParty().randomPartyMemberSay(model, List.of("Wow, what a view!",
                "Fascinating artwork!",
                "These statues really look like they could come alive at any moment.",
                "A good place for contemplation and meditation.",
                "A peacful place."));
        println("Each party member gains 25 XP!");
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            model.getParty().giveXP(model, gc, 25);
        }
    }
}
