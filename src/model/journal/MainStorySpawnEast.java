package model.journal;

import model.Model;
import model.mainstory.*;
import model.map.WorldBuilder;
import model.map.locations.*;

import java.awt.*;
import java.util.List;

public class MainStorySpawnEast extends MainStorySpawnLocation {
    public MainStorySpawnEast() {
        super(new AshtonshireTown().getName(),
              new ArkvaleCastle().getName(),
              new Point(38, 11),
              new UrnTownTown().getName(),
              WorldBuilder.EXPAND_EAST,
              new Point(40, 10),
              new Point(33, 11),
                "Honorable Warriors",
                WorldBuilder.EASTERN_PALACE_LOCATION);
    }

    @Override
    public GainSupportOfRemotePeopleTask makeRemoteKingdomSupportTask(Model model) {
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
}
