package model.states.events;

import model.Model;
import model.states.DailyEventState;
import view.subviews.CollapsingTransition;
import view.subviews.MineSubView;

public class AdvancedMineEvent extends DailyEventState {
    public AdvancedMineEvent(Model model) {
        super(model);
    }

    @Override
    protected void doEvent(Model model) {
        MineSubView mineSubView = new MineSubView();
        CollapsingTransition.transition(model, mineSubView);

        waitForReturn();
    }
}
