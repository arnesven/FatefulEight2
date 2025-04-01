package model.journal;

import model.Model;
import model.mainstory.*;
import model.map.WorldBuilder;
import model.map.locations.*;

import java.util.List;
import java.awt.*;

public class MainStorySpawnWest extends MainStorySpawnLocation {
    public MainStorySpawnWest() {
        super(new LittleErindeTown().getName(),
              new SunblazeCastle().getName(),
              new Point(22, 21),
              new AckervilleTown().getName(),
              WorldBuilder.EXPAND_WEST,
              new Point(11, 25),
              new Point(19, 27),
              "Pirates",
               WorldBuilder.PIRATE_HAVEN_LOCATION);
    }

    @Override
    public GainSupportOfRemotePeopleTask makeRemoteKingdomSupportTask(Model model) {
        return new GainSupportOfPiratesTask(model);
    }

    @Override
    public List<GainSupportOfNeighborKingdomTask> makeNeighborKingdomTasks(Model model) {
        String castle1 = new ArdhCastle().getName();
        String castle2 = new BogdownCastle().getName();
        GainSupportOfNeighborKingdomTask task1 = new GainSupportOfNeighborKingdomByFightingKingdomTask(castle1,
                model.getWorld().getPositionForLocation(model.getWorld().getLocationByName(castle1)), getCastle(), castle2, new Point(25, 25));
        GainSupportOfNeighborKingdomByFightingOrcsTask task2 = new GainSupportOfNeighborKingdomByFightingOrcsTask(castle2,
                model.getWorld().getPositionForLocation(model.getWorld().getLocationByName(castle2)), getCastle(), castle1, new Point(11, 12));
        return List.of(task1, task2);
    }
}
