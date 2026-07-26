package model.journal;

import model.Model;
import model.characters.appearance.AdvancedAppearance;
import model.mainstory.*;
import model.mainstory.honorable.GainSupportOfHonorableWarriorsTask;
import model.map.*;
import model.map.locations.*;
import util.MyPair;

import java.awt.*;
import java.util.List;
import java.util.Map;

public class MainStorySpawnEast extends MainStorySpawnLocation {
    private AdvancedAppearance arabella = new SouthernArabellaAppearance();

    public MainStorySpawnEast() {
        super(new AshtonshireTown().getName(),
              new ArkvaleCastle().getName(),
              new Point(38, 11),
              new UrnTownTown().getName(),
              WorldBuilder.EXPAND_EAST,
              new Point(40, 11),
              new Point(33, 11),
                "Honorable Warriors",
                WorldBuilder.EASTERN_PALACE_LOCATION,
              new MainStoryPastData(new Point(7, 2), new Point(30, 10),
                      TombLocation.SHAKMA_TOMB, RuinsLocation.RONK_RUINS, TombLocation.KZINRIC_TOMB,
                      new Point(1, 2),
                      new Point(1, 13),
                      new Point(17, 13),
                      new Point(18, 0)));
    }

    @Override
    public GainSupportOfRemotePeopleTask makeRemotePeopleSupportTask(Model model) {
        return new GainSupportOfHonorableWarriorsTask();
    }

    @Override
    public List<GainSupportOfNeighborKingdomTask> makeNeighborKingdomTasks(Model model) {
        String castle1 = new BogdownCastle().getName();
        String castle2 = new ArdhCastle().getName();
        GainSupportOfNeighborKingdomTask task1 = new GainSupportOfNeighborKingdomByFightingKingdomTask(castle1,
                model.getWorld().getPositionForLocation(model.getWorld().getLocationByName(castle1)), getCastle(), castle2, new Point(19, 10));
        GainSupportOfNeighborKingdomByFightingOrcsTask task2 = new GainSupportOfNeighborKingdomByFightingOrcsTask(castle2,
                model.getWorld().getPositionForLocation(model.getWorld().getLocationByName(castle2)), getCastle(), castle1, new Point(38, 20));
        return List.of(task1, task2);
    }

    @Override
    protected void addPastRoads(World w) {
        addRoads(w, List.of( // Road from the fishing village to the north-west crossroads.
                        new MyPair<>(new Point(9, 1), Direction.SOUTH | Direction.NORTH_EAST),
                        new MyPair<>(new Point(9, 2), Direction.NORTH | Direction.SOUTH_WEST),
                        new MyPair<>(new Point(8, 2), Direction.NORTH_EAST | Direction.SOUTH_WEST),
                        new MyPair<>(new Point(7, 3), Direction.NORTH_EAST | Direction.NORTH_WEST),
                        new MyPair<>(new Point(6, 2), Direction.SOUTH_EAST | Direction.SOUTH_WEST),
                        new MyPair<>(new Point(5, 3), Direction.SOUTH | Direction.NORTH_WEST | Direction.NORTH_EAST)
                ));

        addRoads(w, List.of( // Road from the north-west crossroads to the mines of despair.
                        new MyPair<>(new Point(4, 2), Direction.SOUTH_EAST | Direction.SOUTH_WEST),
                        new MyPair<>(new Point(3, 3), Direction.NORTH_EAST | Direction.NORTH_WEST),
                        new MyPair<>(new Point(2, 2), Direction.SOUTH_EAST | Direction.NORTH_WEST)
                        ));

        addRoads(w, List.of( // Road from the north-west crossroads to the City of Ronk.
                new MyPair<>(new Point(5, 4), Direction.NORTH | Direction.SOUTH),
                new MyPair<>(new Point(5, 5), Direction.NORTH | Direction.SOUTH),
                new MyPair<>(new Point(5, 6), Direction.NORTH | Direction.SOUTH),
                new MyPair<>(new Point(5, 7), Direction.NORTH | Direction.SOUTH_EAST),
                new MyPair<>(new Point(6, 7), Direction.NORTH_WEST | Direction.SOUTH_EAST),
                new MyPair<>(new Point(7, 8), Direction.NORTH_WEST | Direction.SOUTH),
                new MyPair<>(new Point(7, 9), Direction.NORTH | Direction.SOUTH_EAST)));
    }

    @Override
    protected void addPastFishingVillages(World w) {
        w.getHex(new Point(12, 11)).setLocation(new PastFishingVillage(Direction.SOUTH_EAST));
        w.getHex(new Point(16, 7)).setLocation(new PastFishingVillage(Direction.NORTH_EAST));
    }

    @Override
    public AdvancedAppearance getArabellaAppearance() {
        return arabella;
    }
}
