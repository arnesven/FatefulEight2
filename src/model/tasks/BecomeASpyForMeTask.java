package model.tasks;

import model.Model;
import model.Party;
import model.Summon;
import model.characters.GameCharacter;
import model.classes.Classes;
import model.classes.Skill;
import model.classes.SpyClass;
import model.map.UrbanLocation;
import model.states.events.ChangeClassEvent;

import java.util.List;

public class BecomeASpyForMeTask extends SummonTask {
    private final Summon summon;
    private final UrbanLocation location;

    public BecomeASpyForMeTask(Summon summon, Model model, UrbanLocation location) {
        super(model);
        this.summon = summon;
        this.location = location;
    }

    @Override
    protected void doEvent(Model model) {
        println(location.getLordName() + ": \"I would like you to be my eyes and ears in town. " +
                "Do you think you can do that? I'll pay you of course.\"");
        model.getParty().randomPartyMemberSay(model, List.of("I think " + heOrShe(location.getLordGender()) + " wants us to spy on the townspeople."));
        if (hasASpy(model.getParty())) {
            println(location.getLordName() + ": \"You seem to already have the proper skills for this. " +
                    "All you need to do is to sign this contract.\"");
            signContract(model);
        } else {
            println(location.getLordName() + ": \"But it would seem you do not have the right skill set for this.\"");
            println(location.getLordName() + " is offering to train you in the ways of spycraft, ");
            new ChangeClassEvent(model, Classes.SPY).areYouInterested(model);
            if (hasASpy(model.getParty())) {
                println(location.getLordName() + ": \"Splendid! Now all you need to do is to sign this contract.\"");
                signContract(model);
            } else {
                println(location.getLordName() + ": \"I do not wish to employ anyone who is not properly trained for the task.\"");
            }
        }

    }

    private void signContract(Model model) {
        print("Do you accepts? (Y/N) ");
        if (yesNoInput()) {
            println(location.getLordName() + ": \"Splendid. Here's your first paycheck. Now go find out what's happening in my town.\"");
            summon.increaseStep();
            println("The party receives 20 gold.");
            model.getParty().addToGold(20);
        } else {
            println(location.getLordName() + ": \"Uh, okay. But come back if you change your mind.\"");
        }
    }

    private boolean hasASpy(Party party) {
        for (GameCharacter gc : party.getPartyMembers()) {
            if (gc.getCharClass() instanceof SpyClass) {
                return true;
            }
        }
        return false;
    }
}