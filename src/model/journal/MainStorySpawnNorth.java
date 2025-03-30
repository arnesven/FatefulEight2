package model.journal;

import model.Model;
import model.mainstory.GainSupportOfRemotePeopleTask;
import model.mainstory.GainSupportOfVikingsTask;
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
                WorldBuilder.VIKING_VILLAGE_LOCATION,
                List.of(new ArkvaleCastle().getName(), new SunblazeCastle().getName()));
    }

    @Override
    public GainSupportOfRemotePeopleTask makeRemoteKingdomSupportTask(Model model) {
        return new GainSupportOfVikingsTask(model);
    }
}
