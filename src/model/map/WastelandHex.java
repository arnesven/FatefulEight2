package model.map;

import model.Model;
import model.combat.WastelandCombatTheme;
import model.map.locations.WatelandLocation;
import model.states.DailyEventState;
import model.states.events.*;
import util.MyRandom;
import view.MyColors;
import view.subviews.CombatTheme;
import view.subviews.ImageSubView;
import view.subviews.SubView;

import java.util.List;

public class WastelandHex extends WorldHex {
    private static SubView subView = new ImageSubView("wasteland", "THE WASTELANDS",
            "A barren and perilous wasteland.", true);


    public WastelandHex(int roads, int rivers, HexLocation loc, int state) {
        super(MyColors.TAN, roads, rivers, loc, state);
    }

    public WastelandHex(int roads, int rivers, int state) {
        this(roads, rivers, new WatelandLocation(), state);
    }

    @Override
    public String getTerrainName() {
        return "wasteland";
    }

    @Override
    protected DailyEventState generateTerrainSpecificEvent(Model model) {
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
                new NoEventState(model)
        ));
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
}
