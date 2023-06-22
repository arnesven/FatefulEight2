package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.classes.Classes;
import model.journal.JournalEntry;
import model.map.UrbanLocation;
import model.states.DailyEventState;

import java.util.List;

public class MayorEvent extends DailyEventState {
    public MayorEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        UrbanLocation town = (UrbanLocation) model.getCurrentHex().getLocation();
        if (!model.getParty().getSummons().containsKey(town.getPlaceName())) {
            println("A group of nobles are engrossed in a conversation on a street corner " +
                    "when a wagon, fully loaded with huge wine barrels passes by. Suddenly a barrel " +
                    "tumbles off the wagon and rolls straight at one of the nobles.");
            leaderSay("Watch out!");
            println(model.getParty().getLeader().getName() + " pushes the noble out of the way. The wine barrel " +
                    "smashes into a stone wall and splashes wine all over the street.");
            model.getParty().randomPartyMemberSay(model, List.of("Darn, I wasn't quick enough with my cup.",
                    "That was a close one.", "Somebody could have been killed!", "What a mess."));
            leaderSay("Are you alright sir?");
            boolean gender = town.getLordGender();
            println("The noble picks " + himOrHer(gender) +
                    "self off the ground and dusts off " + hisOrHer(gender) + " robes.");
            showExplicitPortrait(model, model.getLordPortrait(town), "Noble");
            showRandomPortrait(model, Classes.NOB, "Noble");
            portraitSay("That would have been the end of me. If not for you. Thank you.");
            model.getParty().partyMemberSay(model, model.getParty().getLeader(), List.of("Damn wine sellers!",
                    "Be more careful in the future", "My pleasure.", "Don't worry about it."));
            portraitSay("You must be rewarded, uhm, won't you take this gold?");
            print("Accept the gold? (Y/N) ");
            if (yesNoInput()) {
                println("The party gains 10 gold.");
                model.getParty().addToGold(10);
                leaderSay("Thank you.");
                portraitSay("Safe journeys friend.");
            } else {
                leaderSay("Don't even think about it. Hopefully one day somebody will do the same for me.");
                portraitSay("What an honourable sentiment! I wish more people were like you in this town. " +
                        "Please come visit me later. Here take this.");
                println("The noble hands you a slip of paper, then excuses himself.");
                model.getParty().addSummon(town);
                if (model.getParty().size() > 1) {
                    GameCharacter other = model.getParty().getPartyMember(model.getParty().size()-1);
                    model.getParty().partyMemberSay(model, other, "Who was that?");
                    println("You look at the piece of paper.");
                    leaderSay("Well, friends, we just saved the " + town.getLordTitle().toLowerCase() + ".");
                    JournalEntry.printJournalUpdateMessage(model);
                }
            }
        } else {
            new NoEventState(model).doEvent(model);
        }
    }
}
