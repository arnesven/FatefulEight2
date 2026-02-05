package model.states.dailyaction;

import model.Model;
import model.map.UrbanLocation;
import model.map.locations.FishingVillageLocation;
import model.states.GameState;
import model.states.dailyaction.town.CharterBoatAtDocks;

public class FishingVillageActionState extends TownishDailyActionState {
    public FishingVillageActionState(Model model, FishingVillageLocation loc) {
        super(model, true, loc, false, false);
        addNode(5, 3, new BuyFoodStandNode());
    }

    @Override
    public void addTavernNode(Model model, boolean freeLodging, UrbanLocation urbanLocation) {
        // No tavern here!
    }


    @Override
    protected void addTravelNodes(Model model, boolean hasWaterAccess, UrbanLocation urbanLocation) {
        super.addTravelNodes(model, hasWaterAccess, urbanLocation);
        if (model.getDay() % urbanLocation.charterBoatEveryNDays() == 0) {
            addNode(3, 0, new CharterBoatAtDocks(model));
        }
    }
}
