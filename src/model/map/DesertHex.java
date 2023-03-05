package model.map;

import model.Model;
import model.states.DailyEventState;
import model.states.events.*;
import util.MyRandom;
import view.subviews.SubView;
import view.subviews.ImageSubView;
import view.MyColors;

import java.util.List;

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
        int roll = MyRandom.rollD10();
        if (3 <= roll && roll <= 4 && getLocation() instanceof MountainLocation) {
            return MountainHex.generateMountainEvent(model);
        } else if (5 <= roll) {
            return MyRandom.sample(List.of(
                    // new ManticoreEvent(model),
                    new WoundedAdventurerEvent(model),
                    new MirageEvent(model),
                    // new QuicksandEvent(model),
                    new LostEvent(model),
                    new HermitEvent(model),
                    new DeadBodyEvent(model),
                    new ChestEvent(model),
                    // new ScorpionEvent(model)
                    // new VulturesEvent(model)
                    // new OasisEvent(model)
                    new DehydrationEvent(model),
                    new DehydrationEvent(model),
                    new DehydrationEvent(model),
                    new DehydrationEvent(model)

            ));
        }
        return HillsHex.generateHillsEvent(model);
    }
}
