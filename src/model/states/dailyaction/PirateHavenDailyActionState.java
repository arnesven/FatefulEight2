package model.states.dailyaction;

import model.Model;
import model.map.UrbanLocation;
import model.map.locations.PirateHavenLocation;
import model.states.dailyaction.town.CharterBoatAtDocks;

public class PirateHavenDailyActionState extends TownishDailyActionState {
    public PirateHavenDailyActionState(Model model, PirateHavenLocation pirateHavenLocation) {
        super(model, true, pirateHavenLocation, false, false);
    }

    @Override
    protected void addTravelNodes(Model model, boolean isCoastal, UrbanLocation urbanLocation) {
        super.addTravelNodes(model, isCoastal, urbanLocation);
        if (model.getDay() % urbanLocation.charterBoatEveryNDays() == 0) {
            addNode(3, 0, new CharterBoatAtDocks(model));
        }
    }
}
