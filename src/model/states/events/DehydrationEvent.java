package model.states.events;

import model.Model;
import model.characters.PersonalityTrait;
import model.states.DailyEventState;

import java.util.ArrayList;
import java.util.List;

public class DehydrationEvent extends DailyEventState {
    public DehydrationEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        model.getParty().randomPartyMemberSay(model,
                List.of("My throat is like sand.", "I'm sooo thirsty.",
                        "Anybody else feel like a drink?",
                        "Goodness, I'm sweating buckets here."));
        randomSayIfPersonality(PersonalityTrait.jovial, new ArrayList<>(),
                "I would tell a joke, but my humor is to dry!");
        println("The intense sun beats down mercilessly. The party " +
                "members are severely perspiring and must constantly " +
                "replenish their bodily fluids. Each party member consumes an extra ration.");
        model.getParty().addToFood(-model.getParty().size());
    }
}
