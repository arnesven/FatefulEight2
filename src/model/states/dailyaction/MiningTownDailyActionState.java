package model.states.dailyaction;

import model.Model;
import model.map.UrbanLocation;
import model.map.locations.MiningTownLocation;
import model.states.GameState;
import model.states.dailyaction.tavern.AncientCityTavernNode;

public class MiningTownDailyActionState extends TownishDailyActionState {
    public MiningTownDailyActionState(Model model, MiningTownLocation miningTownLocation) {
        super(model, false, miningTownLocation, false, false);
        addNode(2, 1, new MiningHoleNode());
    }

    public void addTavernNode(Model model, boolean freeLodging, UrbanLocation urbanLocation) {
        blockPosition(urbanLocation.getTavernPosition().x, urbanLocation.getTavernPosition().y-2);
        super.addNode(urbanLocation.getTavernPosition().x, urbanLocation.getTavernPosition().y,
                new AncientCityTavernNode(model, freeLodging));
    }
}
