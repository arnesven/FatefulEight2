package model.journal;

import model.Model;
import model.characters.appearance.AdvancedAppearance;
import model.mainstory.*;
import model.mainstory.jungletribe.GainSupportOfJungleTribeTask;
import model.map.RuinsLocation;
import model.map.TombLocation;
import model.map.World;
import model.map.WorldBuilder;
import model.map.locations.*;

import java.awt.Point;
import java.util.List;

public class MainStorySpawnSouth extends MainStorySpawnLocation {
    private AdvancedAppearance arabella = new SouthernArabellaAppearance();

    public MainStorySpawnSouth() {
        super(new SouthMeadhomeTown().getName(),
              new ArdhCastle().getName(),
              new Point(27, 21),
              new BullsVilleTown().getName(),
              WorldBuilder.EXPAND_SOUTH,
              new Point(36, 29),
              new Point(33, 23),
                "Jungle Tribe",
                WorldBuilder.JUNGLE_VILLAGE_LOCATION,
                new MainStoryPastData(new Point(18, 1), new Point(14, 23), RuinsLocation.ZAND_RUINS, TombLocation.UZOCTL_TOMB, TombLocation.XALARDIUM_TOMB));
    }

    @Override
    public GainSupportOfRemotePeopleTask makeRemotePeopleSupportTask(Model model) {
        return new GainSupportOfJungleTribeTask(model);
    }

    @Override
    public List<GainSupportOfNeighborKingdomTask> makeNeighborKingdomTasks(Model model) {
        String castle1 = new ArkvaleCastle().getName();
        String castle2 = new SunblazeCastle().getName();
        GainSupportOfNeighborKingdomTask task1 = new GainSupportOfNeighborKingdomByFightingKingdomTask(castle1,
                model.getWorld().getPositionForLocation(model.getWorld().getLocationByName(castle1)), getCastle(), castle2, new Point(36, 17));
        GainSupportOfNeighborKingdomByFightingOrcsTask task2 = new GainSupportOfNeighborKingdomByFightingOrcsTask(castle2,
                model.getWorld().getPositionForLocation(model.getWorld().getLocationByName(castle2)), getCastle(), castle1, new Point(14, 27));
        return List.of(task1, task2);
    }

    @Override
    public World buildPastWorld() {
        return WorldBuilder.buildPastWorld(getPastUpperLeftCornerPoint());
    }

    @Override
    public AdvancedAppearance getArabellaAppearance() {
        return arabella;
    }
}
