package model.map;

import model.Model;
import model.states.DailyEventState;
import model.states.events.OrcBandEvent;
import util.MyRandom;
import view.subviews.SubView;
import view.subviews.ImageSubView;
import view.MyColors;

import java.util.List;

public class HillsHex extends WorldHex {
    private static final ImageSubView subview = new ImageSubView("hills", "THE HILLS", "You are traveling in the hills.", true);

    public HillsHex(int roads, int rivers) {
        super(MyColors.GREEN, roads, rivers, new HillsLocation());
    }

    @Override
    public String getTerrainName() {
        return "hills";
    }

    @Override
    protected SubView getSubView() {
        return subview;
    }

    @Override
    protected DailyEventState generateTerrainSpecificEvent(Model model) {
        return MyRandom.sample(List.of(new OrcBandEvent(model)));
        //return new NoEventState(model);
    }
}
