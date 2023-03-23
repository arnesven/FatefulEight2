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
        return generateHillsEvent(model);
    }


    public static DailyEventState generateHillsEvent(Model model) {
        if (MyRandom.rollD10() >= 5) {
            return MyRandom.sample(List.of(
                    new OrcBandEvent(model),
                    new WizardsAbodeEvent(model),
                    new MineEvent(model),
                    new HalfOrcEvent(model),
                    new BarbarianEvent(model),
                    new StormEvent(model),
                    new WatchtowerEvent(model),
                    new DwarfEvent(model),
                    new CairnEvent(model),
                    new HalflingVillage(model),
                    new PegasusEvent(model),
                    new TrollEvent(model),
                    new BanditEvent(model),
                    new AbandonedShackEvent(model),
                    // new OrcishStronghold(model) // TODO
                    new CaveEvent(model),
                    new CaveEvent(model),
                    new CaveEvent(model)
            ));
        }
        return new NoEventState(model);
    }

    @Override
    public MyPair<Point, Integer> getDailyActionMenuPositionAndAnchor() {
        return DailyActionMenu.LOWER_LEFT_CORNER;
    }
}
