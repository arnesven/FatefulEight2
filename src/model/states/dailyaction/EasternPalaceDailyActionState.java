package model.states.dailyaction;

import model.Model;
import model.map.UrbanLocation;
import model.map.locations.EasternPalaceLocation;
import model.states.GameState;
import model.states.dailyaction.tavern.EasternTavernNode;
import model.states.dailyaction.tavern.PirateTavernNode;
import model.states.dailyaction.tavern.TavernNode;

public class EasternPalaceDailyActionState extends TownishDailyActionState {
    public EasternPalaceDailyActionState(Model model, EasternPalaceLocation easternPalaceLocation) {
        super(model, false, easternPalaceLocation, false, false);
    }

    public void addTavernNode(Model model, boolean freeLodging, UrbanLocation urbanLocation) {
        super.addNode(urbanLocation.getTavernPosition().x, urbanLocation.getTavernPosition().y,
                new EasternTavernNode(freeLodging));
    }
}
