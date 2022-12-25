package model.map;

import model.Model;
import model.states.DailyEventState;
import model.states.NoEventState;
import view.subviews.SubView;
import view.subviews.ImageSubView;
import view.MyColors;

public class MountainHex extends WorldHex {
    private static SubView subView = new ImageSubView("themountains", "THE MOUNTAINS", "You are traveling in the mountains.", true);

    public MountainHex(int roads, int rivers) {
        super(MyColors.GREEN, roads, rivers, new MountainLocation());
    }

    @Override
    public String getTerrainName() {
        return "mountains";
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
