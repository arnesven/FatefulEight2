package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Classes;
import model.states.DailyEventState;

public class ArcherEvent extends DailyEventState {
    public ArcherEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        showRandomPortrait(model, Classes.MAR, "Archer");
        println("Out on the grounds, a skilled archer puts arrow after " +
                "arrow right in the bullseye. He gladly gives all who will " +
                "listen a free lesson in marksmanship.");
        println("Each party member gains 10 experience.");
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            model.getParty().giveXP(model, gc, 10);
        }
        print("The archer also offers to train you in the ways of being a Marksman, ");
        ChangeClassEvent change = new ChangeClassEvent(model, Classes.MAR);
        change.areYouInterested(model);
    }
}
