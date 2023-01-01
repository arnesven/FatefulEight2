package model.map;

import model.Model;
import model.states.DailyEventState;
import model.states.NoEventState;
import model.states.events.*;
import util.MyRandom;
import view.subviews.SubView;
import view.subviews.ImageSubView;
import view.MyColors;

import java.util.ArrayList;
import java.util.List;

public class FieldsHex extends WorldHex {

    private static SubView subView =  new ImageSubView("thefields", "THE FIELDS", "You are traveling in the farmlands.", true);

    public FieldsHex(int roads, int rivers) {
        super(MyColors.LIGHT_YELLOW, roads, rivers, new FieldsLocation());
    }

    @Override
    public String getTerrainName() {
        return "fields";
    }

    @Override
    protected SubView getSubView() {
        return subView;
    }

    @Override
    protected DailyEventState generateTerrainSpecificEvent(Model model) {
        if (MyRandom.rollD10() >= 5) {
            List<DailyEventState> events = new ArrayList<>();
            events.add(new HalflingEvent(model));     // 4%
            events.add(new PlowingFieldsEvent(model)); // 4%
            events.add(new GuestEvent(model));  // 16%
            events.add(new GuestEvent(model));  // 16%
            events.add(new GuestEvent(model));  // 16%
            events.add(new GuestEvent(model));  // 16%
            events.add(new ChoppingWoodEvent(model)); // 4%
            events.add(new FaeriesEvent(model));
            events.add(new OrchardEvent(model));
            events.add(new OrchardEvent(model));
            events.add(new FarmersChildEvent(model));
            events.add(new BanditRaidEvent(model));
            return MyRandom.sample(events);
        }
        return new NoEventState(model);
    }
}
