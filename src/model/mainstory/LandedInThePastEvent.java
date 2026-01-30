package model.mainstory;

import model.Model;
import model.states.DailyEventState;
import model.states.GameState;
import util.MyLists;

public class LandedInThePastEvent extends DailyEventState {
    public LandedInThePastEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        MyLists.forEach(model.getParty().getPartyMembers(),
                gc -> model.getParty().forceEyesClosed(gc, false));
        leaderSay("I feel like I've been turned inside out. What happened to the castle?");
        if (model.getParty().size() > 1) {
            partyMemberSay(model.getParty().getRandomPartyMember(model.getParty().getLeader()), "Where are we?");
            leaderSay("No idea. That crazy machine must have teleported us somewhere.");
            partyMemberSay(model.getParty().getRandomPartyMember(model.getParty().getLeader()),
                    "Just pick a direction to walk in, I guess.");
        } else {
            leaderSay("Where am I? That crazy machine must have teleported me somewhere.");
        }
    }
    
}
