package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.states.DailyEventState;
import util.MyRandom;

import java.util.List;

public class MeditationEvent extends DailyEventState {
    public MeditationEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        showEventCard("Meditation", "The priests here insist that everybody meditate at least an hour a day. " +
                "Most of the party rejects this idea out of hand, except...");
        GameCharacter gc = MyRandom.sample(model.getParty().getPartyMembers());
        model.getParty().partyMemberSay(model, gc, List.of("Aw heck, I'll give it a try",
                "It can't be complete nonsense", "I'll do it. Worth a try."));
        if (MyRandom.rollD10() > 6) {
            println(gc.getName() + " got 10 XP.");
            model.getParty().giveXP(model, gc, 10);
            model.getParty().partyMemberSay(model, gc, List.of("I actually feel much better!"));
        } else {
            println("However, the meditation has no effect.");
            model.getParty().partyMemberSay(model, gc, List.of("OK, I admit. It's just nonsense after all."));
        }
    }
}
