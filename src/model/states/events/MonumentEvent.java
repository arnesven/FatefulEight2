package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.states.DailyEventState;

public class MonumentEvent extends DailyEventState {
    public MonumentEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("A large stone slab, adorned with a plaque. This " +
                "monument was erected to honor the memory of a group " +
                "of heroes who once saved this world from a terrible fate. " +
                "The plaque has a detailed account of how the world was " +
                "saved from eternal damnation. Reading the text inspires " +
                "dialogue and contemplation among the party members. ");
        println("Each party member gains 25 experience!");
        model.getLog().waitForAnimationToFinish();
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            model.getParty().giveXP(model, gc, 25);
        }
    }
}
