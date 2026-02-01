package model.map;

import model.Model;
import model.states.DailyEventState;
import model.states.events.*;
import util.MyRandom;
import view.combat.CombatTheme;
import view.combat.DesertCombatTheme;
import view.subviews.SubView;
import view.subviews.ImageSubView;
import view.MyColors;

import java.awt.*;
import java.util.List;

public class DesertHex extends WorldHex {

    private static SubView subView = new ImageSubView("thedesert", "THE DESERT", "You are in an arid wasteland.", true);

    public DesertHex(int roads, int rivers, HexLocation location, int state) {
        super(MyColors.YELLOW, roads, rivers, location, state);
    }

    @Override
    public String getTerrainName() {
        return "desert";
    }

    @Override
    protected SubView getSubView(Model model) {
        return subView;
    }

    @Override
    public DailyEventState generateTerrainSpecificEvent(Model model) {
        int roll = MyRandom.rollD10();
        if (roll == 2) {
            return new DogEvent(model);
        } else if (3 <= roll && roll <= 4 && getLocation() instanceof MountainLocation) {
            return MountainHex.generateMountainEvent(model);
        } else if (5 <= roll) {
            return MyRandom.sample(List.of(
                    new ManticoreEvent(model),
                    new WoundedAdventurerEvent(model),
                    new MirageEvent(model),
                    new QuicksandEvent(model),
                    new LostEvent(model),
                    new HermitEvent(model),
                    new DeadBodyEvent(model),
                    new ChestEvent(model),
                    new ScorpionEvent(model),
                    new VulturesEvent(model),
                    new OasisEvent(model),
                    new AbandonedShackEvent(model),
                    new DehydrationEvent(model),
                    new DehydrationEvent(model),
                    new RedKnightEvent(model),
                    new DehydrationEvent(model),
                    new DehydrationEvent(model),
                    new LostExplorerEvent(model)
            ));
        } else if (getLocation() instanceof HillsLocation) {
            return HillsHex.generateHillsEvent(model);
        }
        return new NoEventState(model);
    }

    @Override
    public DailyEventState generateDogEvent(Model model) {
        return MyRandom.sample(List.of(new DeadBodyEvent(model), new WoundedAdventurerEvent(model), new AbandonedShackEvent(model)));
    }

    @Override
    public DailyEventState getNightTimeAmbushEvent(Model model) {
        if (MyRandom.rollD10() == 1) {
            return new ScorpionNightAttackEvent(model);
        }
        return null;
    }

    @Override
    public CombatTheme getCombatTheme() {
        return new DesertCombatTheme();
    }

    @Override
    public String getTerrainDescription() {
        return "Desert areas are arid, sandy expanses where the sun has long since dried up any rivers or lakes. These " +
                "areas are dangerous because of the harsh climate but also because of the dangerous creatures who live there. " +
                "Such creatures are often hardened by the elements and hostile to any who come near them.";
    }

    @Override
    public ResourcePrevalence getResourcePrevalences() {
        return new ResourcePrevalence(ResourcePrevalence.NON_EXISTENT, ResourcePrevalence.NON_EXISTENT);
    }

    @Override
    public WorldHex makePastSelf(Point oldPosition, Point newPosition) {
        return new PastDesertHex(getRivers(), getState(), makePastLocation());
    }
}
