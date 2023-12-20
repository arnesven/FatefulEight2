package model.states.dailyaction;

import model.Model;
import model.SteppingMatrix;
import model.TimeOfDay;
import model.map.CastleLocation;
import model.states.GameState;
import view.MyColors;
import view.subviews.CastleSubView;
import view.subviews.DailyActionSubView;

import java.awt.*;

public class CastleDailyActionState extends AdvancedDailyActionState {

    private final CastleLocation location;

    public CastleDailyActionState(Model model, CastleLocation castleLocation, boolean freeLodge, boolean freeRations) {
        super(model);
        this.location = castleLocation;
        super.addNode(3, 4, new StayHereNode());
        super.addNode(location.getTavernPosition().x, location.getTavernPosition().y, new TavernNode(freeLodge));
        super.addNode(3, 2, new GoToCastleActionNode(location));
        MyColors groundColor = CastleSubView.GROUND_COLOR;
        if (model.getTimeOfDay() == TimeOfDay.EVENING) {
            groundColor = CastleSubView.GROUND_COLOR_NIGHT;
        }
        super.addNode(0, TOWN_MATRIX_ROWS-1, new CampOutsideOfTownNode(freeRations, groundColor, "Camp outside the castle walls"));
        super.addNode(location.getTravelNodePosition().x, location.getTravelNodePosition().y, new TravelNode(groundColor));
        super.addNode(6, 6, new WorkBenchNode(groundColor));
        addNode(4, 6, new SaveGameNode());
        addNode(7, 1, new FlagPoleNode());
        for (GeneralShopNode shop : location.getShops(model)) {
            addNode(shop.getColumn(), shop.getRow(), shop);
        }
    }

    public CastleDailyActionState(Model model, CastleLocation castleLocation) {
        this(model, castleLocation, false, false);
    }

    @Override
    protected Point getStartingPosition() {
        return new Point(3, 5);
    }

    @Override
    protected DailyActionSubView makeSubView(Model model, AdvancedDailyActionState advancedDailyActionState, SteppingMatrix<DailyActionNode> matrix) {
        return location.makeActionSubView(model, advancedDailyActionState, matrix);
    }
}
