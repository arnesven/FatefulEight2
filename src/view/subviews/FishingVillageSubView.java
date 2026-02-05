package view.subviews;

import model.SteppingMatrix;
import model.map.WaterLocation;
import model.states.dailyaction.AdvancedDailyActionState;
import model.states.dailyaction.DailyActionNode;

public class FishingVillageSubView extends TownishSubView {
    public FishingVillageSubView(AdvancedDailyActionState advancedDailyActionState,
                                 SteppingMatrix<DailyActionNode> matrix) {
        super(advancedDailyActionState, matrix, WaterLocation.coastal, "Fishing Village", 0.2, false, TownSubView.TOWN_HOUSES);
    }

}
