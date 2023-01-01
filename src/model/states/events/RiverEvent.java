package model.states.events;

import model.Model;
import model.states.DailyEventState;
import view.subviews.ImageSubView;
import view.subviews.SubView;

public abstract class RiverEvent extends DailyEventState {

    public static SubView subView = new ImageSubView("river", "RIVER", "You are attempting to cross the river.", true);

    public RiverEvent(Model model) {
        super(model);
    }

    public abstract boolean eventPreventsCrossing(Model model);
}
