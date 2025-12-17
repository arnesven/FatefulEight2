package model.states.events;

import model.GameStatistics;
import model.Model;
import model.characters.PersonalityTrait;
import model.classes.Classes;
import model.classes.Skill;
import model.states.DailyEventState;

import java.util.List;

public class ThievesGuildEvent extends DailyEventState {
    private final ConstableEvent constableEvent;

    public ThievesGuildEvent(Model model) {
        super(model);
        this.constableEvent = new ConstableEvent(model, false);
    }

    @Override
    protected void doEvent(Model model) {
        showEventCard("The party finds a seedy bar and wanders in to rest their legs a bit. " +
                "It doesn't take a lot of eavesdropping to figure out that this is the hideout " +
                "of a thieves guild. The members of the guild don't seem to troubled about " +
                "being found out however and is boasting about an upcoming heist.");
        showRandomPortrait(model, Classes.THF, "Guild Master");
        portraitSay("In fact, we could use a few extra hands on the heist...");
        println("The Guild Master looks the party over, as to appraise your skillfulness...");
        int modifier = DailyEventState.calculatePartyAlignment(model, this);
        if (modifier >= 0) {
            portraitSay("Hmm, but you fellows don't quite look like you're the type. " +
                    "Better sit on the sidelines for this one.");
            model.getParty().randomPartyMemberSay(model, List.of("What prejudice is this?",
                    "You don't even know us!", "This isn't a good idea anyway."));
            println("You leave the seedy bar.");
        } else {
            portraitSay("You lot look like you're the right kind of scoundrels for " +
                    "this type of thing. Are you in?");
            randomSayIfPersonality(PersonalityTrait.mischievous, List.of(model.getParty().getLeader()),
                    "We're not gonna pass on this chance are we?");
            randomSayIfPersonality(PersonalityTrait.lawful, List.of(model.getParty().getLeader()),
                    "We could get in trouble if we go along with this. I would advise against it.");
            print("Are you in? (Y/N) ");
            if (yesNoInput()) {
                portraitSay("That's what I like to hear. Which part of the heist do you think " +
                        "you could help with the most, breaking and entering (Y) or infiltrating the grounds (N)?");
                boolean result = true;
                if (yesNoInput()) {
                    println("You help the thieves break into an estate on the edge of town.");
                    result = model.getParty().doSoloLockpickCheck(model, this, 8);
                } else {
                    println("You help sneak past the guards at an estate on the edge of town.");
                    result = model.getParty().doSoloSkillCheck(model, this, Skill.Sneak, 8);
                }
                result = result && model.getParty().doSoloSkillCheck(model, this, Skill.Search, 7);
                if (result) {
                    println("The heist has been successful and the Guild Master is pleased. He gladly pays you your share of the booty.");
                    model.getParty().earnGold(25);
                    GameStatistics.incrementGoldStolen(25);
                    println("The party gains 25 gold.");
                    leaderSay("We're so bad!");
                } else {
                    println("The heist has gone south and the authorities arrive.");
                    printQuote("Constable", "Hey there! Stop right there!");
                    constableEvent.doEvent(model);
                }
            } else {
                leaderSay("There are better, more honest ways, of earning some gold.");
                println("You leave the seedy bar.");
            }
        }
    }

    @Override
    public boolean haveFledCombat() {
        return constableEvent.haveFledCombat();
    }
}
