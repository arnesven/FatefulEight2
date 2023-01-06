package model.states.dailyaction;

import model.Model;
import model.SteppingMatrix;
import model.states.GameState;
import view.subviews.DailyActionSubView;
import view.subviews.TavernSubView;

import java.awt.*;

public class TavernDailyActionState extends AdvancedDailyActionState {
    private final boolean inTown;

    public TavernDailyActionState(Model model, boolean freeLodging, boolean inTown) {
        super(model);
        this.inTown = inTown;
        addNode(2, 5, new RecruitNode(model));
        addNode(6, 1, new LodgingNode(freeLodging));
        addNode(2, 3, new BuyRationsNode());
        if (inTown) {
            Point doorPos = getDoorPosition();
            addNode(doorPos.x, doorPos.y, new ExitTavernNode());
        } else {
            addNode(7, 8, new TravelFromInnNode());
            addNode(1, 8, new CampOutsideOfTownNode(false));
            addNode(4, 2, new InnShoppingNode(model));
        }
    }

    public static Point getDoorPosition() {
        return new Point(3, 7);
    }

    @Override
    protected Point getStartingPosition() {
        return new Point(3, 4);
    }

    @Override
    protected DailyActionSubView makeSubView(Model model, AdvancedDailyActionState advancedDailyActionState,
                                             SteppingMatrix<DailyActionNode> matrix) {
        return new TavernSubView(advancedDailyActionState, matrix, inTown);
    }
}
