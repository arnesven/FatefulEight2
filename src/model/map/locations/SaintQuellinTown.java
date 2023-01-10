package model.map.locations;

import model.Model;
import model.map.TownLocation;
import model.states.dailyaction.AccessoryShopNode;
import model.states.dailyaction.GeneralShopNode;

import java.awt.*;
import java.util.List;

public class SaintQuellinTown extends TownLocation {
    public SaintQuellinTown() {
        super("Saint Quellin", "Councilman Egon", false);
    }

    @Override
    protected List<GeneralShopNode> getShops(Model model) {
        return List.of(new GeneralShopNode(model, 1, 5),
                new AccessoryShopNode(model, 1, 3));
    }

    @Override
    protected Point getTavernPosition() {
        return new Point(5, 5);
    }
}
