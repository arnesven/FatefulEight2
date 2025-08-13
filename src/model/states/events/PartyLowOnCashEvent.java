package model.states.events;

import model.Model;
import model.characters.GameCharacter;
import model.characters.PersonalityTrait;
import model.states.DailyEventState;
import util.MyRandom;

public class PartyLowOnCashEvent extends DailyEventState {
    public PartyLowOnCashEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        if (model.getParty().size() < 2 || model.getParty().getGold() > 3) {
            new NoEventState(model).doEvent(model);
            return;
        }
        GameCharacter goodGuy = model.getParty().getRandomPartyMember(model.getParty().getLeader());
        println(goodGuy.getFirstName() + " approaches you.");
        partyMemberSay(goodGuy, "Hey. I know we're low on cash... I've been saving a little on the side, and... " +
                "well you can have it. For the good of the party you know.");
        leaderSay("That's very generous of you " + goodGuy.getFirstName() + ". I promise to put it to good use.<3");
        int amount = MyRandom.rollD6();
        if (goodGuy.hasPersonality(PersonalityTrait.generous)) {
            amount *= 2;
            partyMemberSay(goodGuy, "The party is worth it.");
        }
        println("The party gains " + amount + " gold.");
        model.getParty().goldTransaction(amount);
        model.getParty().getLeader().addToAttitude(goodGuy, amount);
        model.getLog().waitForAnimationToFinish();
        showPartyAttitudesSubView(model);
    }
}
