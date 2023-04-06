package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.CharacterClass;
import model.states.DailyEventState;
import util.MyRandom;

import java.util.List;

public class TranceEvent extends DailyEventState {
    public TranceEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("You join in during a chanting session with the monks. Apparently participants sometimes " +
                "enter a trance and unlock hidden potential within themselves.");
        GameCharacter trancer = null;
        for (GameCharacter gc : model.getParty().getPartyMembers()) {
            model.getParty().partyMemberSay(model, gc,
                    List.of("Aaaauuummmm", "Oooooohhhmmm", "Hoooooaaaaa",
                    "Ummmmmmm", "Yaaaaaaa"));
            if (MyRandom.rollD10() > 8) {
                trancer = gc;
            }
        }

        if (trancer == null) {
            model.getParty().randomPartyMemberSay(model, List.of("Well, that was a waste of time..."));
        } else {
            println(trancer.getName() + " has fallen into a trance!");
            CharacterClass newClass = null;
            do {
                newClass = trancer.getClasses()[MyRandom.randInt(trancer.getClasses().length)];
            } while (newClass.id() == trancer.getCharClass().id());
            ChangeClassEvent change = new ChangeClassEvent(model, newClass);
            println("From the deep recess of " + hisOrHer(trancer.getGender()) + " mind, " + trancer.getName() +
                    " is unlocking the skills needed to be a " + newClass.getFullName() + ", ");
            change.areYouInterested(model);
        }

    }
}
