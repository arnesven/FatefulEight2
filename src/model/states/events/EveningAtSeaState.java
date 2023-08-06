package model.states.events;

import model.Model;
import model.states.EveningState;
import model.states.GameState;
import view.subviews.CollapsingTransition;
import view.subviews.ImageSubView;
import view.subviews.SubView;

public class EveningAtSeaState extends EveningState {
    public static SubView subViewShip = new ImageSubView("ship", "EVENING", "You spend the evening on the boat.");

    public EveningAtSeaState(Model model) {
        super(model, false, true, false);
    }

    @Override
    public void setSubView(Model model) {
        CollapsingTransition.transition(model, subViewShip);
    }

    @Override
    protected GameState nextState(Model model) {
        return new TravelByCharteredBoat(model);
    }
}
