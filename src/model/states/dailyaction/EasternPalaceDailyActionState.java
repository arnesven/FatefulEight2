package model.states.dailyaction;

import model.Model;
import model.map.UrbanLocation;
import model.map.locations.EasternPalaceLocation;
import model.states.GameState;
import model.states.dailyaction.tavern.EasternTavernNode;
import model.states.dailyaction.tavern.PirateTavernNode;
import model.states.dailyaction.tavern.TavernNode;

import java.awt.*;

public class EasternPalaceDailyActionState extends TownishDailyActionState {
    public EasternPalaceDailyActionState(Model model, EasternPalaceLocation easternPalaceLocation) {
        super(model, false, easternPalaceLocation, false, false);
        addNode(3, 2, new VisitEasternPalaceNode());
        addNode(6, 8, new WaterMillNode());
        blockPosition(2, 2);
        blockPosition(2, 1);
        blockPosition(4, 2);
        blockPosition(4, 1);
        Point p = easternPalaceLocation.getTavernPosition();
        p.y = p.y - 1;
        blockPosition(p.x, p.y);
    }

    public void addTavernNode(Model model, boolean freeLodging, UrbanLocation urbanLocation) {
        super.addNode(urbanLocation.getTavernPosition().x, urbanLocation.getTavernPosition().y,
                new EasternTavernNode(freeLodging));
    }
}
