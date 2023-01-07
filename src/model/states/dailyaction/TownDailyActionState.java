package model.states.dailyaction;

import model.Model;
import model.SteppingMatrix;
import view.subviews.DailyActionSubView;
import view.subviews.TownSubView;

import java.awt.*;

public class TownDailyActionState extends AdvancedDailyActionState {

    private final boolean isCoastal;

    public TownDailyActionState(Model model, boolean isCoastal, boolean freeLodging, boolean freeRations) {
        super(model);
        super.addNode(3, 4, new StayHereNode());
        super.addNode(6, 1, new ShoppingNode(model));
        super.addNode(1, 4, new TavernNode(freeLodging));
        super.addNode(3, 2, new TownHallNode());
        super.addNode(2, TOWN_MATRIX_ROWS-1, new CampOutsideOfTownNode(freeRations));
        super.addNode(TOWN_MATRIX_COLUMNS-1, TOWN_MATRIX_ROWS-2, new TravelNode());
        addNode(7, 2, new SaveGameNode());
        addNode(2, 0, new GoTheDocksNode(model));
        this.isCoastal = isCoastal;

    }

    public TownDailyActionState(Model model, boolean isCoastal) {
        this(model, isCoastal, false, false);
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
