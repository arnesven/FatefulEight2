package model.states.events;

import model.Model;
import model.map.CastleLocation;
import model.states.DailyEventState;

public class BetOnTournamentEvent extends TournamentEvent  {
    public BetOnTournamentEvent(Model model, CastleLocation castle) {
        super(model, castle);
    }

    @Override
    protected void doEvent(Model model) {

    }
}
