package model.states;

import model.Model;
import util.MyRandom;
import view.subviews.MapSubView;

import java.awt.*;

public class RunAwayState extends TravelState {
    public RunAwayState(Model model) {
        super(model);
    }

    @Override
    protected Point selectDirection(Model model, MapSubView mapSubView) {
        print("You are running away. ");
        return MyRandom.sample(mapSubView.getRunAwayDirections(model));
    }

    @Override
    protected boolean checkRiverCrossing(Model model, MapSubView mapSubView) {
        return false;
    }

    @Override
    protected GameState nextState(Model model) {
        return new EveningState(model);
    }
}
