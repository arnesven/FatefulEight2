package model.journal;

import model.Model;
import model.characters.appearance.AdvancedAppearance;
import model.mainstory.*;
import model.mainstory.honorable.GainSupportOfHonorableWarriorsTask;
import model.map.*;
import model.map.locations.*;

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
    public AdvancedAppearance getArabellaAppearance() {
        return arabella;
    }
}
