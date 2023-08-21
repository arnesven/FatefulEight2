package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Classes;
import model.states.DailyEventState;
import util.MyRandom;

import java.util.List;

public class PlayEvent extends DailyEventState {
    public PlayEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("There's apparently a play running in town. Everybody is talking about it. It is supposed to be pretty good.");
        model.getParty().randomPartyMemberSay(model, List.of("Sounds fun, we should go!",
                "Oh please spare me...", "Could be interesting.", "We rarely do something like this for ourselves, we should take this opportunity.",
                "This sounds like a genuine waste of time."));
        print("The entrance fee is 1 gold per person.");
        int cost = model.getParty().size();
        if (model.getParty().getGold() < cost) {
            println(" But since you cannot afford it anyway you glumly turn away and go about your business.");
        } else {
            print(" Do you pay? (Y/N) ");
            if (yesNoInput()) {
                model.getParty().addToGold(-cost);
                boolean bardGender = MyRandom.randInt(2) == 0;
                println("The play features a romantic tale between an elf prince and a human common waif. " +
                        "It is truly very good. You feel your spirits have indeed been lifted by the play.");
                println("Each party member recovers 1 SP.");
                for (GameCharacter gc : model.getParty().getPartyMembers()) {
                    gc.addToSP(1);
                }
                print("You were very impressed by the lead actor. After the play, you get the chance to talk to " + himOrHer(bardGender) +
                        ". It turns out " + heOrShe(bardGender) + " is a famous travelling minstrel. " + heOrShe(bardGender) + " offers to " +
                        "teach you in the ways of being a bard, ");
                ChangeClassEvent change = new ChangeClassEvent(model, Classes.BRD);
                change.areYouInterested(model);
            }
        }
    }
}
