package model.map.locations;

import model.Model;
import model.map.TownLocation;
import model.states.dailyaction.AccessoryShopNode;
import model.states.dailyaction.GeneralShopNode;

import java.awt.*;
import java.util.List;

public class UrnTownTown extends TownLocation {
    public UrnTownTown() {
        super("Urntown", "Elder Marten", false);
    }

    @Override
    public Point getTavernPosition() {
        return new Point(5, 1);
    }

    @Override
    public List<GeneralShopNode> getShops(Model model) {
        return List.of(new GeneralShopNode(model, 5, 5),
                new AccessoryShopNode(model, 2, 5));
    }

    @Override
    public boolean getLordGender() {
        return false;
    }

    @Override
    public String getGeographicalDescription() {
        return "It lies west of Arkvale Castle, on the other side of the mountains.";
    }
}
