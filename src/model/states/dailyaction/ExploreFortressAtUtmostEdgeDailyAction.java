package model.states.dailyaction;

import model.Model;
import model.actions.DailyAction;
import model.states.ExploreFortressAtUtmostEdgeState;

public class ExploreFortressAtUtmostEdgeDailyAction extends DailyAction {
    public ExploreFortressAtUtmostEdgeDailyAction(Model model, String name) {
        super("Explore Fortress", new ExploreFortressAtUtmostEdgeState(model, name));
    }
}
