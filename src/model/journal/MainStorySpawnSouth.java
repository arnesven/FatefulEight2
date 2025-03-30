package model.journal;

import model.Model;
import model.mainstory.GainSupportOfJungleTribeTask;
import model.mainstory.GainSupportOfRemotePeopleTask;
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
                WorldBuilder.JUNGLE_PYRAMID_LOCATION,
                List.of(new SunblazeCastle().getName(), new ArkvaleCastle().getName()));
    }

    @Override
    public GainSupportOfRemotePeopleTask makeRemoteKingdomSupportTask(Model model) {
        return new GainSupportOfJungleTribeTask(model);
    }
}
