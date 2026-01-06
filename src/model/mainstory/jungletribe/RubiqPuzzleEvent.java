package model.mainstory.jungletribe;

import model.Model;
import model.states.DailyEventState;
import view.subviews.CollapsingTransition;

public class RubiqPuzzleEvent extends DailyEventState {
    public RubiqPuzzleEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        CollapsingTransition.transition(model, new RubiqPuzzleSubView());
    }
}
