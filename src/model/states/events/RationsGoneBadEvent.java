package model.states.events;

import model.Model;
import model.characters.PersonalityTrait;
import model.states.DailyEventState;

import java.util.ArrayList;
import java.util.List;

public class RationsGoneBadEvent extends DailyEventState {
    public RationsGoneBadEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        if (model.getParty().getFood() == 0) {
            new NoEventState(model).doEvent(model);
            return;
        }
        if (model.getParty().size() > 1) {
            model.getParty().randomPartyMemberSay(model, List.of("Yuck, what is that smell?"));
            model.getParty().randomPartyMemberSay(model, List.of("It's coming from one of our knapsacks..."));
            randomSayIfPersonality(PersonalityTrait.critical, List.of(model.getParty().getLeader()),
                    "Who bought this stuff anyway?");
            randomSayIfPersonality(PersonalityTrait.forgiving, new ArrayList<>(),
                    "This happens to everyone sooner or later. Let's just toss it and move on.");
        }
        println("You realize that some of your rations have spoiled, and you must throw them away.");
        int lost = Math.min(model.getParty().getFood(), 5);
        println("The party has lost " + lost + " rations.");
        model.getParty().addToFood(-lost);
    }
}
