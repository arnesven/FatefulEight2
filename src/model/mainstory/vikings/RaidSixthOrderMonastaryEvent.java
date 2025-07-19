package model.mainstory.vikings;

import model.Model;
import model.map.WorldBuilder;
import model.map.WorldHex;
import model.states.GameState;
import model.states.TravelBySeaState;

public class RaidSixthOrderMonastaryEvent extends GameState {
    public RaidSixthOrderMonastaryEvent(Model model) {
        super(model);
    }

    @Override
    public GameState run(Model model) {
        WorldHex destHex = model.getWorld().getHex(WorldBuilder.MONASTERY_POSITION);
        TravelBySeaState.travelBySea(model, destHex, this, TravelBySeaState.SHIP_AVATAR,
                "Faith Island", true, true);
        waitForReturn();
        return model.getCurrentHex().getDailyActionState(model);
    }
}
