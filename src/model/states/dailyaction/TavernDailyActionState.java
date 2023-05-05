package model.states.dailyaction;

import model.Model;
import model.SteppingMatrix;
import model.states.GameState;
import view.subviews.DailyActionSubView;
import view.subviews.TavernSubView;
import view.subviews.TownSubView;

import java.awt.*;

public class TavernDailyActionState extends AdvancedDailyActionState {
    private final boolean inTown;

    public TavernDailyActionState(Model model, boolean freeLodging, boolean inTown) {
        super(model);
        this.inTown = inTown;
        addNode(2, 5, new RecruitNode(model));
        addNode(6, 1, new LodgingNode(freeLodging));
        addNode(2, 3, new BuyRationsNode());
        addNode(5, 5, new TalkToPartyNode());
        if (inTown) {
            Point doorPos = getDoorPosition();
            addNode(doorPos.x, doorPos.y, new ExitTavernNode());
        } else {
            addNode(7, 8, new TravelFromInnNode());
            addNode(1, 8, new CampOutsideOfTownNode(false, TownSubView.GROUND_COLOR, "Make camp outside."));
            addNode(4, 2, new InnShoppingNode(model));
            addNode(2, 1, new SaveGameNode());
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
