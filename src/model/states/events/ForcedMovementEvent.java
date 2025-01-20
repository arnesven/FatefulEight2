package model.states.events;

import model.Model;
import model.states.DailyEventState;

import java.awt.*;
import java.util.List;

public class ForcedMovementEvent extends DailyEventState {
    private final List<Point> path;

    public ForcedMovementEvent(Model model, List<Point> path) {
        super(model);
        this.path = path;
    }

    @Override
    protected void doEvent(Model model) {
        forcedMovement(model, path);
    }
}
