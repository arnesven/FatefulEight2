package model.map;

import model.Model;
import view.combat.WastelandCombatTheme;
import model.map.locations.WastelandLocation;
import model.states.DailyEventState;
import model.states.events.*;
import util.MyRandom;
import view.MyColors;
import view.combat.CombatTheme;
import view.subviews.ImageSubView;
import view.subviews.SubView;

import java.awt.*;
import java.util.List;

public class WastelandHex extends WorldHex {
    private static SubView subView = new ImageSubView("wasteland", "THE WASTELANDS",
            "A barren and perilous wasteland.", true);


    public WastelandHex(int roads, int rivers, HexLocation loc, int state) {
        super(MyColors.TAN, roads, rivers, loc, state);
    }

    public WastelandHex(int roads, int rivers, int state) {
        this(roads, rivers, new WastelandLocation(), state);
    }

    @Override
    public String getTerrainName() {
        return "wasteland";
    }

    @Override
    public DailyEventState generateTerrainSpecificEvent(Model model) {
        int dieRoll = MyRandom.rollD10();
        if (dieRoll == 2) {
            return new DogEvent(model);
        } else if (dieRoll >= 3) {
            return MyRandom.sample(List.of(
                    new GhostTownEvent(model),
                    new WastelandMonsterEvent(model),
                    new WastelandMonsterEvent(model),
                    new LostEvent(model),
                    new StormEvent(model),
                    new ChasmEvent(model),
                    new DehydrationEvent(model),
                    new OrcBandEvent(model),
                    new VulturesEvent(model),
                    new NoEventState(model),
                    new LostExplorerEvent(model),
                    new OrcsBattleEvent(model),
                    new GelatinousBlobEvent(model)
            ));
        }
        return new NoEventState(model);
    }

    @Override
    public DailyEventState generateDogEvent(Model model) {
        return MyRandom.sample(List.of(new DeadBodyEvent(model), new LostExplorerEvent(model), new GhostTownEvent(model)));
    }

    @Override
    public DailyEventState getNightTimeAmbushEvent(Model model) {
        if (MyRandom.rollD10() <= 2) {
            return new BlobNightAttackEvent(model);
        }
        return null;
    }

    @Override
    protected SubView getSubView() {
        return subView;
    }

    @Override
    public String getTerrainDescription() {
        return "Wastelands are barren areas filled with dangers. Perilous pitfalls and vicious monsters can be found in these lands. " +
                "Vegetation is scarce, urban areas even more so. The few towns which were once constructed have long since " +
                "been abandoned. The only encampments to be found house bandits and orcs - beware!";
    }

    @Override
    public CombatTheme getCombatTheme() {
        return new WastelandCombatTheme();
    }

    @Override
    public ResourcePrevalence getResourcePrevalences() {
        return new ResourcePrevalence(ResourcePrevalence.NON_EXISTENT, ResourcePrevalence.POOR);
    }

    @Override
    public WorldHex makePastSelf(Point position) {
        return new PastWastelandHex(getRivers(), getState(), makePastLocation());
    }
}
