package model.journal;

import model.map.WorldBuilder;
import model.map.locations.AckervilleTown;
import model.map.locations.LittleErindeTown;
import model.map.locations.SunblazeCastle;

import java.awt.*;

public class MainStorySpawnWest extends MainStorySpawnLocation {
    public MainStorySpawnWest() {
        super(new LittleErindeTown().getName(),
              new SunblazeCastle().getName(),
              new Point(22, 21),
              new AckervilleTown().getName(),
                WorldBuilder.EXPAND_WEST);
    }
}
