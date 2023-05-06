package model.states.dailyaction;

import model.Model;
import model.SteppingMatrix;
import model.map.UrbanLocation;
import view.subviews.DailyActionSubView;
import view.subviews.TownSubView;

import java.awt.Point;

public class TownDailyActionState extends AdvancedDailyActionState {

    private final boolean isCoastal;
    private final UrbanLocation urbanLocation;

    public TownDailyActionState(Model model, boolean isCoastal, UrbanLocation urbanLocation,
                                boolean freeLodging, boolean freeRations) {
        super(model);
        this.urbanLocation = urbanLocation;
        super.addNode(3, 4, new StayHereNode());
        super.addNode(urbanLocation.getTavernPosition().x, urbanLocation.getTavernPosition().y, new TavernNode(freeLodging));
        super.addNode(3, 3, new TownHallNode());
        super.addNode(0, TOWN_MATRIX_ROWS-1, new CampOutsideOfTownNode(freeRations, TownSubView.GROUND_COLOR, "Make camp on the outskirts of town"));
        super.addNode(4, TOWN_MATRIX_ROWS-1, new WorkBenchNode(TownSubView.GROUND_COLOR));
        super.addNode(urbanLocation.getTravelNodePosition().x, urbanLocation.getTravelNodePosition().y, new TravelNode(TownSubView.GROUND_COLOR));
        addNode(7, 2, new SaveGameNode());
        addNode(7, 1, new FlagPoleNode());
        if (isCoastal && !urbanLocation.noBoat()) {
            addNode(2, 0, new GoTheDocksNode(model));
        }
        for (GeneralShopNode shop : urbanLocation.getShops(model)) {
            addNode(shop.getColumn(), shop.getRow(), shop);
        }
        this.isCoastal = isCoastal;

    }

    public TownDailyActionState(Model model, boolean isCoastal, UrbanLocation urbanLocation) {
        this(model, isCoastal, urbanLocation, false, false);
    }

    @Override
    protected Point getStartingPosition() {
        return new Point(3, 5);
    }

    @Override
    protected DailyActionSubView makeSubView(Model model, AdvancedDailyActionState advancedDailyActionState, SteppingMatrix<DailyActionNode> matrix) {
        return urbanLocation.makeActionSubView(model, advancedDailyActionState, matrix);
    }
}
