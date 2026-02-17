package model.states.dailyaction;

import model.Model;
import model.map.UrbanLocation;
import model.map.locations.VikingVillageLocation;
import model.states.dailyaction.shops.GeneralShopNode;
import model.states.dailyaction.tavern.VikingTavernNode;
import model.states.dailyaction.town.CharterBoatAtDocks;
import model.states.dailyaction.town.SnowyCampOutsideOfTownNode;
import view.MyColors;

import java.awt.*;

public class VikingVillageDailyActionState extends TownishDailyActionState {
    public VikingVillageDailyActionState(Model model, VikingVillageLocation vikingVillageLocation) {
        super(model, true, vikingVillageLocation, false, false);
        Point p = vikingVillageLocation.getTavernPosition();
        p.y = p.y - 1;
        blockPosition(p.x, p.y);

        addNode(3, 3, new VisitLonghouseNode());
    }

    public void addTavernNode(Model model, boolean freeLodging, UrbanLocation urbanLocation) {
        blockPosition(urbanLocation.getTavernPosition().x, urbanLocation.getTavernPosition().y - 1);
        super.addNode(urbanLocation.getTavernPosition().x, urbanLocation.getTavernPosition().y,
                new VikingTavernNode(freeLodging));
    }

    @Override
    protected void addTent(Model model, boolean freeRations, UrbanLocation urbanLocation) {
        super.addNode(0, TOWN_MATRIX_ROWS - 1, new SnowyCampOutsideOfTownNode(freeRations, model,
                "Make camp on the outskirts of town"));
    }

    @Override
    protected void addShopsAndMore(Model model, UrbanLocation urbanLocation) {
        for (GeneralShopNode shop : urbanLocation.getShops(model)) {
            addNode(shop.getColumn(), shop.getRow(), shop);
        }
        super.addNode(2, TOWN_MATRIX_ROWS-1, new WorkBenchNode(model, MyColors.WHITE, MyColors.LIGHT_GRAY));
    }

    @Override
    protected void addTravelNodes(Model model, boolean hasWaterAccess, UrbanLocation urbanLocation) {
        super.addNode(urbanLocation.getTravelNodePosition().x, urbanLocation.getTravelNodePosition().y,
                new TravelNode(model, MyColors.WHITE, MyColors.LIGHT_GRAY));
        addNode(1, 0, new CharterBoatAtDocks(model));
        addNode(4, 0, new LongboatNode(model));
    }
}
