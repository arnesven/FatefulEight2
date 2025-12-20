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
    protected SubView getSubView(Model model) {
        return subView;
    }

    @Override
    public DailyEventState generateTerrainSpecificEvent(Model model) {
        int roll = MyRandom.rollD10();
        if (roll == 4) {
            return new DogEvent(model);
        } else if (roll >= 5) {
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
            events.add(new SecretGardenEvent(model));
            events.add(new RareBirdEvent(model));
            events.add(new CampSiteEvent(model));
            // FEATURE: LadyOfTheLakeEvent
            // FEATURE: CentaurEvent
            return MyRandom.sample(events);
        }
        return new NoEventState(model);
    }

    @Override
    public DailyEventState generateDogEvent(Model model) {
        return MyRandom.sample(List.of(new CaveEvent(model), new ShrineEvent(model), new AbandonedShackEvent(model)));
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

    @Override
    public DailyEventState getNightTimeAmbushEvent(Model model) {
        if (MyRandom.rollD10() == 1) {
            return new WolfNightAttackEvent(model);
        }
        return null;
    }

    @Override
    public ResourcePrevalence getResourcePrevalences() {
        return new ResourcePrevalence(ResourcePrevalence.FAIR, ResourcePrevalence.POOR);
    }

    @Override
    public WorldHex makePastSelf(Point position) {
        return new PastDeepWoodsHex(getRivers(), getState());
    }
}
