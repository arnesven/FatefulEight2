package model.map;

import model.Model;
import model.states.DailyEventState;
import model.states.events.NoEventState;
import view.subviews.SubView;
import view.subviews.ImageSubView;
import view.MyColors;

public class TundraHex extends WorldHex {

    private static SubView subView = new ImageSubView("thetundra", "THE TUNDRA", "You are surrounded by a frozen landscape.", true);

    public TundraHex(int roads, int rivers, HexLocation location) {
        super(MyColors.WHITE, roads, rivers, location);
    }

    @Override
    public String getTerrainName() {
        return "tundra";
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
