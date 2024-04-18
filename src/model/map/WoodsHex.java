package model.map;

import model.Model;
import model.states.DailyEventState;
import model.states.events.NoEventState;
import model.states.events.*;
import util.MyPair;
import util.MyRandom;
import view.subviews.DailyActionMenu;
import view.subviews.SubView;
import view.subviews.ImageSubView;
import view.MyColors;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class WoodsHex extends WorldHex {

    private static SubView subView = new ImageSubView("thewoods", "THE WOODS", "You are in the woods.", true);

    public WoodsHex(int roads, int rivers, HexLocation location, int state) {
        super(MyColors.GREEN, roads, rivers, location, state);
    }

    public WoodsHex(int roads, int rivers, int state) {
        this(roads, rivers, new WoodsLocation(false), state);
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
        return new SecretGardenEvent(model); /*
        if (MyRandom.rollD10() >= 5) {
            List<DailyEventState> events = new ArrayList<>();
            events.add(new LumberMillEvent(model));
            events.add(new WolfEvent(model));
            events.add(new ElvenCampEvent(model));
            events.add(new ElfEvent(model));
            events.add(new SorcerersTowerEvent(model));
            events.add(new WitchHutEvent(model));
            events.add(new VipersEvent(model));
            events.add(new FaeriesEvent(model));
            events.add(new HalflingEvent(model));
            events.add(new AbandonedShackEvent(model));
            events.add(new LovelyClearingEvent(model));
            events.add(new CaveEvent(model));
            events.add(new PaladinEvent(model));
            events.add(new HuntingEvent(model));
            events.add(new FrogmenScoutsEvent(model));
            events.add(new ShrineEvent(model));
            return MyRandom.sample(events);
        }
        return new NoEventState(model); */
    }

    @Override
    public MyPair<Point, Integer> getDailyActionMenuPositionAndAnchor() {
        return DailyActionMenu.UPPER_RIGHT_CORNER;
    }

    @Override
    public String getTerrainDescription() {
        return "Wooded areas often contain many creatures, most often benign but some are hostile. " +
                "You commonly encounter elves and halfings in woods.";
    }
}
