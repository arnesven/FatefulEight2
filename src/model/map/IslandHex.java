package model.map;

import model.Model;
import model.map.locations.GrassCorner;
import model.states.DailyEventState;
import model.states.events.NoEventState;
import view.subviews.ImageSubView;
import view.subviews.SubView;

public class IslandHex extends PlainsHex {

    private static final SubView subView = new ImageSubView("island", "ISLAND", "You are on an island...", true);

    public IslandHex(int roads, int rivers, int state) {
        super(roads, rivers, new GrassCorner(), state);
    }

    @Override
    public String getTerrainName() {
        return "island";
    }

    @Override
    public DailyEventState generateTerrainSpecificEvent(Model model) {
        return new NoEventState(model);
    }

    @Override
    protected SubView getSubView(Model model) {
        return subView;
    }
}
