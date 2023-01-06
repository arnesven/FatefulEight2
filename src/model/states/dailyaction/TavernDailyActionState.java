package model.states.dailyaction;

import model.Model;
import model.SteppingMatrix;
import model.states.GameState;
import view.subviews.DailyActionSubView;
import view.subviews.TavernSubView;

import java.awt.*;

public class TavernDailyActionState extends AdvancedDailyActionState {
    public TavernDailyActionState(Model model, boolean freeLodging) {
        super(model);
        addNode(1, 3, new RecruitNode());
        addNode(6, 1, new LodgingNode(freeLodging));
        addNode(3, 7, new ExitTavernNode());
    }

    @Override
    protected Point getStartingPosition() {
        return new Point(3, 5);
    }

    @Override
    protected DailyActionSubView makeSubView(Model model, AdvancedDailyActionState advancedDailyActionState,
                                             SteppingMatrix<DailyActionNode> matrix) {
        return new TavernSubView(advancedDailyActionState, matrix);
    }
}
