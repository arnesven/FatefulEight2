package model.map;

import model.Model;
import model.states.DailyEventState;
import model.states.events.*;
import util.MyPair;
import util.MyRandom;
import view.subviews.DailyActionMenu;
import view.subviews.SubView;
import view.subviews.ImageSubView;
import view.MyColors;

import java.awt.*;
import java.util.List;

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
        if (MyRandom.rollD10() >= 5) {
            return MyRandom.sample(List.of(
                    new StormEvent(model),
                    new UnicornEvent(model),
                    new StoneCircleEvent(model),
                    new BerriesEvent(model),
                    new NomadCampEvent(model),
                    new HalflingVillage(model)
            ));
        }
        return new NoEventState(model);
    }

    @Override
    public MyPair<Point, Integer> getDailyActionMenuPositionAndAnchor() {
        return DailyActionMenu.LOWER_RIGHT_CORNER;
    }
}
