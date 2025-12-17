package model.states.events;

import model.GameStatistics;
import model.Model;
import model.characters.PersonalityTrait;
import model.classes.Classes;
import model.states.DailyEventState;
import util.MyRandom;

import java.util.ArrayList;
import java.util.List;

public class LetterFromMotherEvent extends DailyEventState {
    private static final String GOT_THIS_EVENT = "GotLetterFromMother";
    private static final int GOLD_FROM_MOTHER = 75;

    public LetterFromMotherEvent(Model model) {
        super(model);
    }

    @Override
    public boolean exclusiveToOriginalWorld() {
        return true;
    }

    public static DailyEventState generateEvent(Model model) {
        if (!model.getSettings().getMiscFlags().containsKey(GOT_THIS_EVENT) &&
                model.getDay() > 14 && GameStatistics.getGoldEarned() < 150) {
            return new LetterFromMotherEvent(model);
        }
        return null;
    }

    @Override
    protected void doEvent(Model model) {
        model.getSettings().getMiscFlags().put(GOT_THIS_EVENT, true);
        showRandomPortrait(model, Classes.None, "Courier");
        showEventCard("A courier catches up to you and asks you to stop while " + heOrShe(getPortraitGender()) +
                " catches " + hisOrHer(getPortraitGender()) + " breath.");
        portraitSay("'" + model.getParty().getLeader().getFullName() + "' - that's you right? I have a package for you.");
        println(model.getParty().getLeader().getFirstName() + " opens the package, inside is a letter.");
        leaderSay("Hmm... Oh! It's from my mother.");
        randomSayIfPersonality(PersonalityTrait.jovial, List.of(model.getParty().getLeader()), "Time for you to come home for dinner?");
        println("Under the letter is a pouch. It clinks in a promising way when you lift it up.");
        leaderSay("Oh mother, you shouldn't have...");
        println("The party gains " + GOLD_FROM_MOTHER + " gold!");
        model.getParty().earnGold(GOLD_FROM_MOTHER);
        if (model.getParty().size() > 1) {
            partyMemberSay(model.getParty().getRandomPartyMember(model.getParty().getLeader()), "What does the letter say?");
        }
        println(model.getParty().getLeader().getFirstName() + " reads the letter out loud.");
        String sellWhat = MyRandom.sample(List.of("comic book collection", "action figure collection", "collection of dress-up clothes",
                "large dollhouse", "stamp collection"));
        leaderSay("'Dear " + model.getParty().getLeader().getFirstName() + ". Since you have not been home for " +
                "quite some time I've been cleaning out your room. I'm turning it into an art studio. I took the liberty of " +
                "selling some of your things. In particular, I sold your " + sellWhat + ". I hope you don't mind.");
        leaderSay("Drat.");
        randomSayIfPersonality(PersonalityTrait.jovial, List.of(model.getParty().getLeader()), "And you were saving that for your own kids one day!");
        leaderSay("'Inside this package are some of the proceeds from the sale. Make good use of them and good luck " +
                "in your adventuring career. Know that you always have a home to come home to here. Albeit without your own room. " +
                "Lots of hugs and kisses. Your Mother.'");
        randomSayIfPersonality(PersonalityTrait.romantic, List.of(model.getParty().getLeader()), "Awww! That's so sweet!3");
        leaderSay("Well we sure can use the money. Thanks mom.");
    }
}
