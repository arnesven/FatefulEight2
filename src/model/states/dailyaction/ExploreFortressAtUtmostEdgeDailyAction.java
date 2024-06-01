package model.states.dailyaction;

import model.Model;
import model.actions.DailyAction;
import model.states.ExploreFortressAtUtmostEdgeState;
import model.states.FortressAtUtmostEdgeState;

public class ExploreFortressAtUtmostEdgeDailyAction extends DailyAction {
    public ExploreFortressAtUtmostEdgeDailyAction(Model model) {
        super("Explore Fortress", new FortressAtUtmostEdgeState(model));
    }
}
