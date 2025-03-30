package model.journal;

import model.Model;
import model.mainstory.GainSupportOfPiratesTask;
import model.mainstory.GainSupportOfRemotePeopleTask;
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
               WorldBuilder.PIRATE_HAVEN_LOCATION,
               List.of(new ArdhCastle().getName(), new BogdownCastle().getName()));
    }

    @Override
    public GainSupportOfRemotePeopleTask makeRemoteKingdomSupportTask(Model model) {
        return new GainSupportOfPiratesTask(model);
    }
}
