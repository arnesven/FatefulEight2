package model.journal;

import model.Model;
import model.mainstory.*;
import model.map.WorldBuilder;
import model.map.locations.*;

import java.awt.Point;
import java.util.List;

public class MainStorySpawnSouth extends MainStorySpawnLocation {
    public MainStorySpawnSouth() {
        super(new SouthMeadhomeTown().getName(),
              new ArdhCastle().getName(),
              new Point(27, 21),
              new BullsVilleTown().getName(),
              WorldBuilder.EXPAND_SOUTH,
              new Point(36, 29),
              new Point(33, 23),
                "Jungle Tribe",
                WorldBuilder.JUNGLE_PYRAMID_LOCATION);
    }

    @Override
    public GainSupportOfRemotePeopleTask makeRemoteKingdomSupportTask(Model model) {
        return new GainSupportOfJungleTribeTask(model);
    }

    @Override
    public List<GainSupportOfNeighborKingdomTask> makeNeighborKingdomTasks(Model model) {
        String castle1 = new ArkvaleCastle().getName();
        String castle2 = new SunblazeCastle().getName();
        GainSupportOfNeighborKingdomTask task1 = new GainSupportOfNeighborKingdomByFightingKingdomTask(castle1,
                model.getWorld().getPositionForLocation(model.getWorld().getLocationByName(castle1)), getCastle(), castle2, new Point(36, 17));
        GainSupportOfNeighborKingdomByFightingOrcsTask task2 = new GainSupportOfNeighborKingdomByFightingOrcsTask(castle2,
                model.getWorld().getPositionForLocation(model.getWorld().getLocationByName(castle2)), getCastle(), castle1, new Point(13, 26));
        return List.of(task1, task2);
    }
}
