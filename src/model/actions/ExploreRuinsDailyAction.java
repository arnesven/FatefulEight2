package model.actions;

import model.Model;
import model.states.ExploreRuinsState;

public class ExploreRuinsDailyAction extends DailyAction {
    public ExploreRuinsDailyAction(Model model, String ruinsName) {
        super("Explore Ruins", new ExploreRuinsState(model, ruinsName, "Ruins"));
    }
}
