package model.journal;

import model.Model;
import model.mainstory.*;
import model.map.WorldBuilder;
import model.map.locations.*;

import java.awt.*;
import java.util.List;

public class MainStorySpawnNorth extends MainStorySpawnLocation {
    public MainStorySpawnNorth() {
        super(new EbonshireTown().getName(),
              new BogdownCastle().getName(),
              new Point(19, 18),
              new EastDurhamTown().getName(),
              WorldBuilder.EXPAND_NORTH,
              new Point(17, 10),
              new Point(18, 14),
                "Vikings",
                WorldBuilder.VIKING_VILLAGE_LOCATION);
    }

    @Override
    public GainSupportOfRemotePeopleTask makeRemoteKingdomSupportTask(Model model) {
        return new GainSupportOfVikingsTask(model);
    }

    @Override
    public List<GainSupportOfNeighborKingdomTask> makeNeighborKingdomTasks(Model model) {
        String castle1 = new SunblazeCastle().getName();
        String castle2 = new ArkvaleCastle().getName();
        GainSupportOfNeighborKingdomTask task1 = new GainSupportOfNeighborKingdomByFightingKingdomTask(castle1,
                model.getWorld().getPositionForLocation(model.getWorld().getLocationByName(castle1)), getCastle(), castle2, new Point(15, 19));
        GainSupportOfNeighborKingdomByFightingOrcsTask task2 = new GainSupportOfNeighborKingdomByFightingOrcsTask(castle2,
                model.getWorld().getPositionForLocation(model.getWorld().getLocationByName(castle2)), getCastle(), castle1, new Point(36, 8));
        return List.of(task1, task2);
    }
}
