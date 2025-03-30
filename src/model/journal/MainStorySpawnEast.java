package model.journal;

import model.Model;
import model.mainstory.GainSupportOfHonorableWarriorsTask;
import model.mainstory.GainSupportOfRemotePeopleTask;
import model.map.WorldBuilder;
import model.map.locations.*;

import java.awt.*;
import java.util.List;

public class MainStorySpawnEast extends MainStorySpawnLocation {
    public MainStorySpawnEast() {
        super(new AshtonshireTown().getName(),
              new ArkvaleCastle().getName(),
              new Point(38, 11),
              new UrnTownTown().getName(),
              WorldBuilder.EXPAND_EAST,
              new Point(40, 10),
              new Point(33, 11),
                "Honorable Warriors",
                WorldBuilder.FAR_EASTERN_TOWN_LOCATION,
                List.of(new BogdownCastle().getName(), new ArdhCastle().getName()));
    }

    @Override
    public GainSupportOfRemotePeopleTask makeRemoteKingdomSupportTask(Model model) {
        return new GainSupportOfHonorableWarriorsTask();
    }
}
