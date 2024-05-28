package model.map;

import model.Model;
import model.map.locations.FortressAtUtmostEdgeLocation;
import model.states.DailyEventState;

public class FortressCaveHex extends CaveHex {
    public FortressCaveHex(int state) {
        super(Direction.NONE, state, new FortressAtUtmostEdgeLocation());
    }

    @Override
    protected boolean canHaveExit() {
        return false;
    }

    @Override
    public DailyEventState generateEvent(Model model) {
        return super.generateEvent(model);
    }
}
