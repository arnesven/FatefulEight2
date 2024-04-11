package model.states;

import model.Model;
import model.map.Direction;
import model.map.World;
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
            if (model.isInCaveSystem()) {
                if (model.getCurrentHex().getRoadInDirection(Direction.getDirectionForDxDy(model.getParty().getPosition(), dir))) {
                    result.add(dir);
                }
            } else {
                if (!model.getWorld().crossesRiver(model.getParty().getPosition(),
                        Direction.getDirectionForDxDy(model.getParty().getPosition(), dir))
                        && !movesOutsideMap(model.getParty().getPosition(), dir)) {
                    result.add(dir);
                }
            }
        }
        return MyRandom.sample(result);
    }

    @Override
    protected boolean checkForRiding(Model model) {
        println("You run away to another hex.");
        model.getParty().getHorseHandler().someHorsesRunAway(model);
        return false;
    }

    protected boolean movesOutsideMap(Point position, Point dir) {
        Point p2 = new Point(position);
        getModel().getWorld().move(p2, dir.x, dir.y);
        return (p2.x == position.x && p2.y == position.y);
    }

    @Override
    protected boolean checkForOverEncumberance(Model model) {
        return true;
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
        return model.getCurrentHex().getEveningState(model, false, false);
    }
}
