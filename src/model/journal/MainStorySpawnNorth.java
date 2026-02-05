package model.journal;

import model.Model;
import model.characters.appearance.AdvancedAppearance;
import model.mainstory.*;
import model.mainstory.vikings.GainSupportOfVikingsTask;
import model.map.RuinsLocation;
import model.map.TombLocation;
import model.map.World;
import model.map.WorldBuilder;
import model.map.locations.*;

import java.awt.*;
import java.util.List;

public class MainStorySpawnNorth extends MainStorySpawnLocation {
    private AdvancedAppearance arabella = new DarkElfArabellaAppearance();;

    public MainStorySpawnNorth() {
        super(new EbonshireTown().getName(),
              new BogdownCastle().getName(),
              new Point(19, 18),
              new EastDurhamTown().getName(),
              WorldBuilder.EXPAND_NORTH,
              new Point(17, 10),
              new Point(18, 14),
                "Vikings",
                WorldBuilder.VIKING_VILLAGE_LOCATION,
                new MainStoryPastData(new Point(1, 9), new Point(14, 4), TombLocation.HAARFAGRE_TOMB, RuinsLocation.URH_RUINS, RuinsLocation.GROUND_RUINS)); // TODO
    }

    @Override
    public GainSupportOfRemotePeopleTask makeRemotePeopleSupportTask(Model model) {
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

    @Override
    public World buildPastWorld() {
        return WorldBuilder.buildPastWorld(getPastUpperLeftCornerPoint());
    }

    @Override
    public AdvancedAppearance getArabellaAppearance() {
        return arabella;
    }
}
