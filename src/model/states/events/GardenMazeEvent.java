package model.states.events;

import model.Model;
import model.states.DailyEventState;
import view.subviews.CollapsingTransition;
import view.subviews.GardenMazeSubView;

public class GardenMazeEvent extends DailyEventState {
    public GardenMazeEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        CollapsingTransition.transition(model, new GardenMazeSubView());
        waitForReturnSilently();
    }
}
