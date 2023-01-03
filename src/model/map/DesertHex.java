package model.map;

import model.Model;
import model.states.DailyEventState;
import model.states.events.NoEventState;
import view.subviews.SubView;
import view.subviews.ImageSubView;
import view.MyColors;

public class DesertHex extends WorldHex {

    private static SubView subView = new ImageSubView("thedesert", "THE DESERT", "You are in an arid wasteland.", true);

    public DesertHex(int roads, int rivers, HexLocation location) {
        super(MyColors.YELLOW, roads, rivers, location);
    }

    @Override
    public String getTerrainName() {
        return "desert";
    }

    @Override
    protected SubView getSubView() {
        return subView;
    }

    @Override
    protected DailyEventState generateTerrainSpecificEvent(Model model) {
        return new NoEventState(model);
    }
}
