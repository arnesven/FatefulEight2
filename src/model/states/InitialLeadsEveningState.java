package model.states;

import model.Model;
import model.characters.GameCharacter;

public class InitialLeadsEveningState extends EveningState {
    public InitialLeadsEveningState(Model model) {
        super(model);
    }

    @Override
    protected void locationSpecificEvening(Model model) {
        super.locationSpecificEvening(model);
        model.getMainStory().setInitialLeadsGiven(true);
        leaderSay("Anybody have any leads? I mean, like jobs or quests?");
        GameCharacter gc = model.getParty().getRandomPartyMember(model.getParty().getLeader());
        model.getParty().partyMemberSay(model, gc, "Well, there's usually some work in towns and castles.");
        leaderSay("Yeah. I think there's a town not far from here. Maybe we should head in that direction.");
        model.getParty().partyMemberSay(model, gc, "And I've heard there are ruins that contain riches.");
        leaderSay("Riches? But dangers too right?");
        model.getParty().partyMemberSay(model, gc, "Probably. We should make sure we're ready for that before " +
                "we get us into more trouble than we can handle.");
        leaderSay("That sounds reasonable. If we could recruit some more party members, " +
                "get our hands on some good gear, and perhaps train up a bit...");
        model.getParty().partyMemberSay(model, gc, "I hear the monks at the temples offer training. For a fee of course.");
        leaderSay("Could be interesting... Well, good night " + gc.getFirstName());
        model.getParty().partyMemberSay(model, gc, "Good night.");
        println("!Your journal has been updated!");
    }
}
