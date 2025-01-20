package model.states.events;

import model.Model;
import model.journal.JournalEntry;
import model.map.Direction;
import model.states.DailyEventState;
import util.MyLists;
import view.LogView;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MoveAwayFromCurrentPositionEvent extends DailyEventState {
    private final int stepsToTake;

    public MoveAwayFromCurrentPositionEvent(Model model, int steps) {
        super(model);
        this.stepsToTake = steps;
    }

    @Override
    protected void doEvent(Model model) {
        model.getLog().addAnimated(LogView.GOLD_COLOR + "Going on the quest has moved your party.\n" +
                LogView.DEFAULT_COLOR);
        List<Point> path = makePathToRemoteLocation(model, stepsToTake);
        forcedMovement(model, path);
    }

    public static List<Point> makePathToRemoteLocation(Model model, int stepsToTake) {
        List<Point> path = new ArrayList<>();
        java.util.List<Point> directions = findValidDirections(model);
        if (directions.isEmpty()) {
            System.out.println("Nowhere to go? Returning");
            return path;
        }
        Point dir = directions.get(0);
        int dirAsInt = Direction.getDirectionForDxDy(model.getParty().getPosition(), dir);
        path.add(makeNextPosition(model.getParty().getPosition(), dir));

        for (int step = 1; step < stepsToTake; ++step) {
            Point currentPosition = path.get(path.size() - 1);
            Point dir2 = Direction.getDxDyForDirection(currentPosition, dirAsInt);
            Point endPoint = makeNextPosition(currentPosition, dir2);
            if (model.getWorld().canTravelTo(model, endPoint)) {
                path.add(endPoint);
            } else {
                break;
            }
        }
        path.add(0, model.getParty().getPosition());
        return path;
    }

    private static Point makeNextPosition(Point currentPosition, Point p) {
        Point nextP = new Point(currentPosition);
        nextP.x += p.x;
        nextP.y += p.y;
        return nextP;
    }

    private static List<Point> findValidDirections(Model model) {
        List<Point> directions = new ArrayList<>(Direction.getDxDyDirections(model.getParty().getPosition()));
        directions = MyLists.filter(directions, (Point dir) ->
                model.getWorld().canTravelTo(model, makeNextPosition(model.getParty().getPosition(), dir)));
        Collections.shuffle(directions);
        return directions;
    }
}
