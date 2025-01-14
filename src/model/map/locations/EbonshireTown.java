package model.map.locations;

import model.Model;
import model.map.TownLocation;
import model.states.dailyaction.AccessoryShopNode;
import model.states.dailyaction.GeneralShopNode;

import java.awt.*;
import java.util.List;

public class EbonshireTown extends TownLocation {
    public EbonshireTown() {
        super("Ebonshire", "Lady Enid", true);
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
    public String getGeographicalDescription() {
        return "It's right where the Ojai river meets the Sea.";
    }
}
