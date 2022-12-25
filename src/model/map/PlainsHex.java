package model.map;

import model.Model;
import model.states.DailyEventState;
import model.states.NoEventState;
import view.subviews.SubView;
import view.subviews.ImageSubView;
import view.MyColors;

public class PlainsHex extends WorldHex {
    private static SubView subView = new ImageSubView("theplains", "THE PLAINS", "You are on the plains.", true);

    public PlainsHex(int roads, int rivers, HexLocation location) {
        super(MyColors.GREEN, roads, rivers, location);
    }

    @Override
    public String getTerrainName() {
        return "plains";
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
