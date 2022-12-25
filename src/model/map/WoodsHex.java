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

public class WoodsHex extends WorldHex {

    private static SubView subView = new ImageSubView("thewoods", "THE WOODS", "You are in the woods.", true);

    public WoodsHex(int roads, int rivers) {
        super(MyColors.GREEN, roads, rivers, new WoodsLocation());
    }

    @Override
    public String getTerrainName() {
        return "woods";
    }

    @Override
    protected SubView getSubView() {
        return subView;
    }

    @Override
    protected DailyEventState generateTerrainSpecificEvent(Model model) {
        if (MyRandom.rollD10() >= 5) {
            List<DailyEventState> events = new ArrayList<>();
            events.add(new LumberMillEvent(model));
            events.add(new WolfEvent(model));
            events.add(new ElvenCampEvent(model));
            events.add(new ElfEvent(model));
            events.add(new SorcerersTowerEvent(model));
            return MyRandom.sample(events);
        }
        return new NoEventState(model);
    }
}
