package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.classes.Classes;
import model.combat.conditions.VampirismCondition;
import model.states.DailyEventState;
import util.MyLists;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class PlayEvent extends DailyEventState {
    public PlayEvent(Model model) {
        super(model);
    }

    @Override
    public GuideData getGuideData() {
        return new GuideData("Go to play", "There's a play running in town");
    }

    @Override
    protected void doEvent(Model model) {
        println("There's apparently a play running in town. Everybody is talking about it. It is supposed to be pretty good.");
        randomSayIfPersonality(PersonalityTrait.critical, new ArrayList<>(),
                "This sounds like a genuine waste of time.");
        randomSayIfPersonality(PersonalityTrait.unkind, new ArrayList<>(),
                "Oh please spare me...");
        randomSayIfPersonality(PersonalityTrait.intellectual, new ArrayList<>(),
                "Could be interesting.");
        randomSayIfPersonality(PersonalityTrait.playful, new ArrayList<>(),
                "Sounds fun, we should go!");
        randomSayIfPersonality(PersonalityTrait.encouraging, new ArrayList<>(),
                "We rarely do something like this for ourselves, we should take this opportunity.");
        print("The entrance fee is 1 gold per person.");
        int cost = model.getParty().size();
        if (model.getParty().getGold() < cost) {
            println(" But since you cannot afford it anyway you glumly turn away and go about your business.");
        } else {
            print(" Do you pay? (Y/N) ");
            if (yesNoInput()) {
                model.getParty().spendGold(cost);
                boolean bardGender = MyRandom.randInt(2) == 0;
                println("The play features a romantic tale between an elf prince and a human common waif. " +
                        "It is truly very good. You feel your spirits have indeed been lifted by the play.");
                println("Each party member recovers 1 SP.");
                MyLists.forEach(model.getParty().getPartyMembers(),
                        (GameCharacter gc) -> gc.addToSP(gc.hasCondition(VampirismCondition.class)?0:1));
                model.getLog().waitForAnimationToFinish();
                showRandomPortrait(model, Classes.BRD, "Minstrel");
                print("You were very impressed by the lead actor. After the play, you get the chance to talk to " + himOrHer(bardGender) +
                        ". It turns out " + heOrShe(bardGender) + " is a famous travelling minstrel. " + heOrShe(bardGender) + " offers to " +
                        "teach you in the ways of being a bard, ");
                ChangeClassEvent change = new ChangeClassEvent(model, Classes.BRD);
                change.areYouInterested(model);
            }
        }
    }
}
