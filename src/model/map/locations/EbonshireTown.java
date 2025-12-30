package model.map.locations;

import model.Model;
import model.map.TownLocation;
import model.map.WaterLocation;
import model.states.dailyaction.shops.AccessoryShopNode;
import model.states.dailyaction.shops.GeneralShopNode;

import java.awt.*;
import java.util.List;

public class EbonshireTown extends TownLocation {
    public static final String NAME = "Ebonshire";

    public EbonshireTown() {
        super(NAME, "Lady Enid", WaterLocation.coastal);
    }

    @Override
    public Point getTavernPosition() {
        return new Point(5, 4);
    }

    @Override
    public List<GeneralShopNode> getShops(Model model) {
        return List.of(new GeneralShopNode(model, 4, 5),
                new AccessoryShopNode(model, 1, 3));
    }

    @Override
    public boolean getLordGender() {
        return true;
    }

    @Override
    public int charterBoatEveryNDays() {
        return 5;
    }

    @Override
    public List<String> getSeaTravelRoutes() {
        return List.of("Ackerville.........Rarely",
                       "Little Erinde......Rarely",
                       "Roukon..........Sometimes",
                       "Lower Theln.....Sometimes",
                       "Cape Paxton.....Sometimes",
                       "East Durham.....Sometimes",
                       "Chartered Boats.Sometimes"); // Sometimes = 4-5)
    }

    @Override
    public String getGeographicalDescription() {
        return "It's right where the Ojai river meets the Sea.";
    }
}
