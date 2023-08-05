package model.journal;

import model.map.WorldBuilder;
import model.map.locations.ArdhCastle;
import model.map.locations.BullsVilleTown;
import model.map.locations.SouthMeadhomeTown;

import java.awt.*;

public class MainStorySpawnSouth extends MainStorySpawnLocation {
    public MainStorySpawnSouth() {
        super(new SouthMeadhomeTown().getName(),
              new ArdhCastle().getName(),
              new Point(27, 21),
              new BullsVilleTown().getName(),
              WorldBuilder.EXPAND_SOUTH,
              new Point(36, 29),
              new Point(33, 23));
    }
}
