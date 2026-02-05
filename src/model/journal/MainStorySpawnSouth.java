package model.journal;

import model.Model;
import model.characters.appearance.AdvancedAppearance;
import model.mainstory.*;
import model.mainstory.jungletribe.GainSupportOfJungleTribeTask;
import model.map.*;
import model.map.locations.*;
import util.MyPair;
import view.MyColors;

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
        World world = WorldBuilder.buildPastWorld(getPastUpperLeftCornerPoint());
        addRoads(world);
        addFishingVillages(world);
        return world;
    }

    private void addFishingVillages(World world) {
        world.getHex(new Point(15, 3)).setLocation(new PastFishingVillage(Direction.SOUTH));
    }

    private void addRoads(World world) {
        List<MyPair<Point, Integer>> pastRoads =
        List.of(
                new MyPair<>(new Point(16, 1), Direction.SOUTH_WEST | Direction.NORTH_EAST),
                new MyPair<>(new Point(17, 1), Direction.SOUTH_WEST),
                new MyPair<>(new Point(18, 0), Direction.SOUTH_WEST | Direction.SOUTH_EAST),
                new MyPair<>(new Point(19, 1), Direction.NORTH_WEST | Direction.SOUTH),
                new MyPair<>(new Point(19, 2), Direction.NORTH)
        );
        for (MyPair<Point, Integer> pair : pastRoads) {
            world.getHex(pair.first).setRoads(pair.second);
        }
    }

    @Override
    public AdvancedAppearance getArabellaAppearance() {
        return arabella;
    }
}
