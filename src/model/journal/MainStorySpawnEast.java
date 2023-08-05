package model.journal;

import model.map.WorldBuilder;
import model.map.locations.ArkvaleCastle;
import model.map.locations.AshtonshireTown;
import model.map.locations.UrnTownTown;

import java.awt.*;

public class MainStorySpawnEast extends MainStorySpawnLocation {
    public MainStorySpawnEast() {
        super(new AshtonshireTown().getName(),
              new ArkvaleCastle().getName(),
              new Point(38, 11),
              new UrnTownTown().getName(),
              WorldBuilder.EXPAND_EAST,
              new Point(40, 10),
              new Point(33, 11));
    }
}
