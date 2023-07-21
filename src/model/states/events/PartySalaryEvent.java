package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.states.DailyEventState;
import util.MyRandom;

public class PartySalaryEvent extends DailyEventState {
    public PartySalaryEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        if (model.getParty().size() < 2 || model.getParty().getGold() < 50) {
            new NoEventState(model).doEvent(model);
            return;
        }

        GameCharacter other = model.getParty().getRandomPartyMember(model.getParty().getLeader());
        println(other.getFirstName() + " approaches you.");
        partyMemberSay(other, "Hey. I was thinking, since things are going pretty good, would it be okay for " +
                "me to ask for an advance on my salary?");
        leaderSay("Hmm... How much were you thinking?");
        int amount = MyRandom.rollD10() + MyRandom.rollD10() + 7;
        partyMemberSay(other, "Well, I was thinking " + amount + " gold.");
        print("Let " + other.getFirstName() + " have " + amount + " gold? (Y/N) ");
        if (yesNoInput()) {
            leaderSay("Okay. You can have it. But don't talk about it too much. We don't have enough for everybody to get an advance right now.");
            partyMemberSay(other, "Sure thing. Thanks " + model.getParty().getLeader().getFirstName() + ".");
            other.addToAttitude(model.getParty().getLeader(), +amount/2);
            model.getParty().addToGold(-amount);
        } else {
            leaderSay("I'm sorry " + other.getFirstName() + " I can't agree to that.");
            partyMemberSay(other, "Why not? I know we have the money. Don't think I haven't been paying attention.");
            leaderSay("The party needs the money. You know, for equipment, lodging and food.");
            partyMemberSay(other, "Fine...#");
            other.addToAttitude(model.getParty().getLeader(), -amount/3);
        }
        model.getLog().waitForAnimationToFinish();
        showPartyAttitudesSubView(model);
    }
}
