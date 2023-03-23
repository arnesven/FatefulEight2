package model.states;

import model.Model;
import model.map.Direction;
import util.MyRandom;
import view.subviews.MapSubView;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class RunAwayState extends TravelState {
    public RunAwayState(Model model) {
        super(model);
    }

    @Override
    protected Point selectDirection(Model model, MapSubView mapSubView) {
        List<Point> result = new ArrayList<>();
        for (Point dir : mapSubView.getDirections(model)) {
            if (!model.getWorld().crossesRiver(model.getParty().getPosition(),
                    Direction.getDirectionForDxDy(model.getParty().getPosition(), dir))) {
                result.add(dir);
            }
        }
        return MyRandom.sample(result);
    }

    @Override
    protected boolean checkRiverCrossing(Model model, MapSubView mapSubView) {
        return false;
    }

    @Override
    protected boolean partyNoLongerOnRoad(Model model, MapSubView mapSubView) {
        return true;
    }

    @Override
    protected GameState nextState(Model model) {
        return new EveningState(model);
    }
}
