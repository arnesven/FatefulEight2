package model.mainstory;

import model.Model;
import model.characters.GameCharacter;

public class GetOutOfCastleEvent extends VisitLordEvent {
    public GetOutOfCastleEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        setCurrentTerrainSubview(model);
        if (model.getParty().size() > 1) {
            GameCharacter other = model.getParty().getRandomPartyMember(model.getParty().getLeader());
            partyMemberSay(other, "What are we doing back here? We'll be caught in no time.");
            leaderSay("Yeah, I recon you're right. Better just slip away quickly before anybody recognizes us.");
        } else {
            println("You start heading toward the keep when you glimpse two guards.");
            leaderSay("This is way to risky. Better just slip away quickly before anybody recognizes me.");
        }
        setFledCombat(true);
    }
}
