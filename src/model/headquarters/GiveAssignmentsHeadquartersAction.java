package model.headquarters;

import model.Model;
import model.states.GameState;
import view.subviews.HeadquartersSubView;

import java.awt.*;

public class GiveAssignmentsHeadquartersAction extends HeadquartersAction {
    public GiveAssignmentsHeadquartersAction(Model model, Point menuLocation, HeadquartersSubView subView) {
        super(model, "Give assignments");
    }

    @Override
    public GameState run(Model model) {
        // TODO!
        return null;
    }
}
