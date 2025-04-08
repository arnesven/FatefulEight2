package model.states.dailyaction;

import model.Model;
import model.map.UrbanLocation;
import model.map.locations.PirateHavenLocation;
import model.states.dailyaction.tavern.PirateTavernNode;
import model.states.dailyaction.tavern.TavernNode;
import model.states.dailyaction.town.CharterBoatAtDocks;
import view.sprites.Sprite;

import java.awt.*;

public class PirateHavenDailyActionState extends TownishDailyActionState {
    public PirateHavenDailyActionState(Model model, PirateHavenLocation pirateHavenLocation) {
        super(model, true, pirateHavenLocation, false, false);
        addNode(0, 1, new PirateBarNode());
    }

    @Override
    protected void addTravelNodes(Model model, boolean isCoastal, UrbanLocation urbanLocation) {
        super.addTravelNodes(model, isCoastal, urbanLocation);
        if (model.getDay() % urbanLocation.charterBoatEveryNDays() == 0) {
            addNode(3, 0, new CharterBoatAtDocks(model));
        }
    }

    public void addTavernNode(Model model, boolean freeLodging, UrbanLocation urbanLocation) {
        super.addNode(urbanLocation.getTavernPosition().x, urbanLocation.getTavernPosition().y, new PirateTavernNode(freeLodging));
    }
}
