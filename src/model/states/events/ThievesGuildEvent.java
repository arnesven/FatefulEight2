package model.states.events;

import model.Model;
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
        println("The party finds a seedy bar and wanders in to rest their legs a bit. " +
                "It doesn't take a lot of eavesdropping to figure out that this is the hideout " +
                "of a thieves guild. The members of the guild don't seem to troubled about " +
                "being found out however and is boasting about an upcoming heist.");
        showRandomPortrait(model, Classes.THF, "Guild Master");
        portraitSay(model, "In fact, we could use a few extra hands on the heist...");
        println("The Guild Master looks the party over, as to appraise your skillfulness...");
        int modifier = DailyEventState.getPartyAlignment(model, this);
        if (modifier >= 0) {
            portraitSay(model, "Hmm, but you fellows don't quite look like you're the type. " +
                    "Better sit on the sidelines for this one.");
            model.getParty().randomPartyMemberSay(model, List.of("What prejudice is this?",
                    "You don't even know us!", "This isn't a good idea anyway."));
            println("You leave the seedy bar.");
        } else {
            portraitSay(model, "You lot look like you're the right kind of scoundrels for " +
                    "this type of thing. Are you in?");
            print("Are you? (Y/N) ");
            if (yesNoInput()) {
                portraitSay(model, "That's what I like to hear. Which part of the heist do you think " +
                        "you could help with the most, breaking and entering (Y) or infiltrating the grounds (N)?");
                boolean result = true;
                if (yesNoInput()) {
                    result = model.getParty().doSoloSkillCheck(model, this, Skill.Security, 8);
                } else {
                    result = model.getParty().doSoloSkillCheck(model, this, Skill.Sneak, 8);
                }
                result = result && model.getParty().doSoloSkillCheck(model, this, Skill.Search, 7);
                if (result) {
                    println("The heist has been successful and the Guild Master is pleased. He gladly pays you your share of the bounty.");
                    model.getParty().addToGold(25);
                    println("The party gains 25 gold.");
                    model.getParty().partyMemberSay(model, model.getParty().getLeader(), "We're so bad!");
                } else {
                    println("The heist has gone south and the authorities arrive.");
                    println("Constable: \"Hey there! Stop right there!\"");
                    constableEvent.doEvent(model);
                }
            } else {
                model.getParty().partyMemberSay(model, model.getParty().getLeader(),
                        "There are better, more honest ways, of earning some gold.");
                println("You leave the seedy bar.");
            }
        }
    }

    @Override
    public boolean haveFledCombat() {
        return constableEvent.haveFledCombat();
    }
}
