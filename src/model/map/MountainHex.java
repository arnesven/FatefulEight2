package model.map;

import model.Model;
import model.states.DailyEventState;
import model.states.events.*;
import util.MyPair;
import util.MyRandom;
import view.subviews.*;
import view.MyColors;

import java.awt.*;
import java.util.List;

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
        return generateMountainEvent(model);
    }

    public static DailyEventState generateMountainEvent(Model model) {
        if (MyRandom.rollD10() >= 5) {
            return MyRandom.sample(List.of(
                    new BarbarianEvent(model),
                    new MinerEvent(model),
                    new MineEvent(model),
                    new StormEvent(model),
                    new DragonEvent(model),
                    new DwarfEvent(model),
                    new HermitEvent(model),
                    new AltarEvent(model),
                    new GiantEvent(model),
                    new WoundedAdventurerEvent(model),
                    new MountainWolfEvent(model),
                    new EaglesEvent(model),
                    new ChasmEvent(model)
                    // new CaveEvent(model)
                    // new CaveEvent(model)
                    // new CaveEvent(model)
            ));
        }
        return new NoEventState(model);
    }

    @Override
    public CombatTheme getCombatTheme() {
        return new MountainCombatTheme();
    }

    @Override
    public MyPair<Point, Integer> getDailyActionMenuPositionAndAnchor() {
        return DailyActionMenu.LOWER_LEFT_CORNER;
    }
}
