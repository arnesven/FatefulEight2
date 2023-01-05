package model.states.dailyaction;

import model.Model;
import model.SteppingMatrix;
import view.subviews.DailyActionSubView;
import view.subviews.TownSubView;

import java.awt.*;

public class TownDailyActionState extends AdvancedDailyActionState {

    private final boolean isCoastal;

    public TownDailyActionState(Model model, boolean isCoastal, boolean isEvening, boolean freeLodging, boolean freeRations) {
        super(model, isEvening);
        super.addNode(3, 4, new StayHereNode());
        super.addNode(6, 1, new ShoppingNode(model));
        super.addNode(1, 4, new TavernNode());
        super.addNode(3, 2, new TownHallNode());
        super.addNode(2, TOWN_MATRIX_ROWS-1, new CampOutsideOfTownNode(freeRations));
        super.addNode(TOWN_MATRIX_COLUMNS-1, TOWN_MATRIX_ROWS-2, new TravelNode());
        this.isCoastal = isCoastal;

    }

    public TownDailyActionState(Model model, boolean isCoastal, boolean isEvening) {
        this(model, isCoastal, isEvening, false, false);
    }

    @Override
    protected Point getStartingPosition() {
        return new Point(3, 5);
    }

    @Override
    protected DailyActionSubView makeSubView(Model model, AdvancedDailyActionState advancedDailyActionState, SteppingMatrix<DailyActionNode> matrix) {
        return new TownSubView(this, matrix, isCoastal);
    }
}
