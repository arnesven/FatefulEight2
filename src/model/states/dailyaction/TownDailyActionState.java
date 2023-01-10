package model.states.dailyaction;

import model.Model;
import model.SteppingMatrix;
import view.subviews.DailyActionSubView;
import view.subviews.TownSubView;

import java.awt.Point;
import java.util.List;

public class TownDailyActionState extends AdvancedDailyActionState {

    private final boolean isCoastal;
    private final String townName;

    public TownDailyActionState(Model model, String townName, boolean isCoastal, List<GeneralShopNode> shops, Point tavernPosition,
                                boolean freeLodging, boolean freeRations) {
        super(model);
        this.townName = townName;
        super.addNode(3, 4, new StayHereNode());
        super.addNode(tavernPosition.x, tavernPosition.y, new TavernNode(freeLodging));
        super.addNode(3, 3, new TownHallNode());
        super.addNode(0, TOWN_MATRIX_ROWS-1, new CampOutsideOfTownNode(freeRations));
        super.addNode(TOWN_MATRIX_COLUMNS-1, TOWN_MATRIX_ROWS-2, new TravelNode());
        addNode(7, 2, new SaveGameNode());
        if (isCoastal) {
            addNode(2, 0, new GoTheDocksNode(model));
        }
        for (GeneralShopNode shop : shops) {
            addNode(shop.getColumn(), shop.getRow(), shop);
        }
        this.isCoastal = isCoastal;

    }

    public TownDailyActionState(Model model, String townName, boolean isCoastal, List<GeneralShopNode> shops, Point tavernPosition) {
        this(model, townName, isCoastal, shops, tavernPosition, false, false);
    }

    @Override
    protected Point getStartingPosition() {
        return new Point(3, 5);
    }

    @Override
    protected DailyActionSubView makeSubView(Model model, AdvancedDailyActionState advancedDailyActionState, SteppingMatrix<DailyActionNode> matrix) {
        return new TownSubView(this, matrix, isCoastal, townName);
    }
}
