package model.map;

import model.Model;
import model.states.DailyEventState;
import model.states.events.*;
import util.MyRandom;
import view.subviews.CombatTheme;
import view.subviews.SubView;
import view.subviews.ImageSubView;
import view.MyColors;
import view.subviews.TundraCombatTheme;

import java.util.List;

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
        int roll = MyRandom.rollD10();
        if (3 <= roll && roll <= 4 && getLocation() instanceof MountainLocation) {
            return MountainHex.generateMountainEvent(model);
        } else if (5 <= roll) {
            return MyRandom.sample(List.of(
                    new ChestEvent(model),
                    new SnowyBeastEvent(model),
                    new BarbarianEvent(model),
                    new DeadBodyEvent(model),
                    new LostEvent(model),
                    new GiantEvent(model),
                    new WoundedAdventurerEvent(model),
                    new HermitEvent(model),
                    new StormEvent(model),
                    new StormEvent(model),
                    new BearEvent(model),
                    new ColdEvent(model),
                    new ColdEvent(model),
                    new MountainWolfEvent(model),
                    new OutpostEvent(model)

            ));
        }
        return new NoEventState(model);
    }

    @Override
    public CombatTheme getCombatTheme() {
        return new TundraCombatTheme();
    }
}
