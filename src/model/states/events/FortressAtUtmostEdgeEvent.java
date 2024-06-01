package model.states.events;

import model.Model;
import model.actions.DailyAction;
import model.characters.GameCharacter;
import model.map.locations.FortressAtUtmostEdgeLocation;
import model.ruins.DungeonMaker;
import model.ruins.FinalDungeonLevel;
import model.ruins.RuinsDungeon;
import model.states.DailyEventState;
import model.states.ExploreRuinsState;
import model.states.GameState;
import model.states.dailyaction.ExploreFortressAtUtmostEdgeDailyAction;

public class FortressAtUtmostEdgeEvent extends DailyEventState {
    public FortressAtUtmostEdgeEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        leaderSay("What is this place?");
        if (model.getParty().size() > 1) {
            GameCharacter gc = model.getParty().getRandomPartyMember(model.getParty().getLeader());
            partyMemberSay(gc, "Looks like some kind of evil temple.");
        }
        leaderSay("Something's written here: 'Fortress of at the Utmost Edge'... I wonder what that means.");
        print("Do you enter? (Y/N) ");
        if (!yesNoInput()) {
            leaderSay("Looks... foreboding. Better not disturb this place.");
        } else {
            leaderSay("Looks exciting. Let's head inside.");
            model.getSettings().getMiscFlags().put(FortressAtUtmostEdgeLocation.HAS_VISITED, true);
            DailyAction action = new ExploreFortressAtUtmostEdgeDailyAction(model);
            action.getState().run(model);
        }
    }
}
