package model.states.dailyaction;

import model.Model;
import model.actions.DailyAction;
import model.states.fatue.FortressAtUtmostEdgeState;

public class ExploreFortressAtUtmostEdgeDailyAction extends DailyAction {
    public ExploreFortressAtUtmostEdgeDailyAction(Model model) {
        super("Explore Fortress", new FortressAtUtmostEdgeState(model));
    }
}
