package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Classes;
import model.states.DailyEventState;

public class JesterEvent extends DailyEventState {
    public JesterEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("The court jester is not only a funny fellow, but has the " +
                "voice of an angel. He sings a lovely ballad of a long " +
                "forgotten kingdom and the romance between an elf prince " +
                "and a human princess. The party feels much refreshed " +
                "when going to bed tonight.");
        println("Each character recovers 1 SP.");
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            gc.addToSP(1);
        }
        print("The jester is kind enough to offer to train you in the ways of being a Bard, ");
        ChangeClassEvent change = new ChangeClassEvent(model, Classes.BRD);
        change.areYouInterested(model);
    }
}
