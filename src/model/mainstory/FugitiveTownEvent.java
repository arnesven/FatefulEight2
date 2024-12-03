package model.mainstory;

import model.Model;
import model.characters.GameCharacter;
import model.map.CastleLocation;
import model.states.DailyEventState;

public class FugitiveTownEvent extends DailyEventState {
    public FugitiveTownEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        CastleLocation castle = model.getWorld().getCastleByName(model.getMainStory().getCastleName());
        println("As you approach the outskirts of town you can clearly see " +
                castle.getLordTitle() + " " + castle.getLordName() + "'s forces moving about.");
        leaderSay("It's no good, " + castle.getLordName() + " has got " + hisOrHer(castle.getLordGender()) +
                " people all over the place here. We'll be caught in no time.");
        if (model.getParty().size() > 1) {
            GameCharacter other = model.getParty().getRandomPartyMember(model.getParty().getLeader());
            partyMemberSay(other, "I agree. We're safer in the wilderness than in urban areas right now. We " +
                    "should stay clear of this place.");
        }
        leaderSay("Let's go.");
        setFledCombat(true);
    }
}
