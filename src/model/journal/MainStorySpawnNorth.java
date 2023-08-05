package model.journal;

import model.map.WorldBuilder;
import model.map.locations.BogdownCastle;
import model.map.locations.EastDurhamTown;
import model.map.locations.EbonshireTown;

import java.awt.*;

public class MainStorySpawnNorth extends MainStorySpawnLocation {
    public MainStorySpawnNorth() {
        super(new EbonshireTown().getName(),
              new BogdownCastle().getName(),
              new Point(19, 18),
              new EastDurhamTown().getName(),
              WorldBuilder.EXPAND_NORTH,
              new Point(17, 10),
              new Point(18, 14));
    }
}
