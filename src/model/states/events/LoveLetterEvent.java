package model.states.events;

import model.Model;
import model.classes.Classes;
import model.classes.Skill;
import model.states.DailyEventState;

public class LoveLetterEvent extends DailyEventState {
    public LoveLetterEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        println("You are pacing through town when suddenly something on the ground catches your eye. At first, " +
                "you take it for just a piece of trash, but then you realize it's something else. You pick it up.");
        model.getParty().partyMemberSay(model, model.getParty().getLeader(), "Hmm, a letter. Looks quite fancy. " +
                "It's addressed to 'Maggie', but who could have sent it?");
        println("Do you open the letter? (Y/N) ");
        int rollBonus = 0;
        if (yesNoInput()) {
            rollBonus += 3;
            println("This appears to be a love letter to somebody named Maggie. Although the content does not reveal " +
                    "the author directly, it is quite lengthy and contains a number of clues which should make tracking him or her down " +
                    "somewhat easier.");
        }
        println("Do you try to return the letter to the sender (Y) or do you attempt to deliver it to Maggie (N)? ");
        if (yesNoInput()) {
            boolean result = model.getParty().doCollaborativeSkillCheck(model, this, Skill.SeekInfo, 10 - rollBonus);
            if (result) {
                showRandomPortrait(model, Classes.ART, "Doug");
                println("You finally find a carpenter named Doug who admits to writing the letter.");
                portraitSay(model, "I must have dropped it while returning from the market this morning!");
                int reward = 15 - rollBonus * 4;
                if (rollBonus != 0) {
                    println("Doug is visibly annoyed that you have opened the letter and read " +
                            "the contents but hands you a few coins for your trouble.");
                } else {
                    println("Doug thanks you profusely for finding the letter and rewards you with some gold.");
                }
                println("The party receives " + reward + " gold.");
                model.getParty().addToGold(reward);
            } else {
                println("After spending an hour trying to find the sender of the letter without any luck, you simply give up and " +
                        "put the letter back on the ground where you found it.");
            }
        } else {
            boolean result = model.getParty().doCollaborativeSkillCheck(model, this, Skill.SeekInfo, 8);
            if (result) {
                showRandomPortrait(model, Classes.NOB, "Maggie");
                println("You finally find a nobleman's daughter named Maggie. She isn't expecting a letter but " +
                        "after reading it she turns bright pink and admits to being the intended recipient.");
                portraitSay(model, "Please let me reward you. If not for you, I would have never received this and my poor " +
                        "paramour would believe that I snubbed him.");
                int reward = 10;
                println("The party gains " + reward + " gold.");
                model.getParty().addToGold(reward);
            } else {
                println("After spending an hour trying to find Maggie without any luck, you simply give up and " +
                        "put the letter back on the ground where you found it.");
            }
        }
    }
}
