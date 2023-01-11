package model.map.locations;

import model.Model;
import model.map.TownLocation;
import model.states.dailyaction.GeneralShopNode;
import model.states.dailyaction.WeaponShopNode;

import java.awt.*;
import java.util.List;

public class CapePaxtonTown extends TownLocation {
    public CapePaxtonTown() {
        super("Cape Paxton", "Mayor Dargola", true);
    }

    @Override
    public Point getTavernPosition() {
        return new Point(5, 4);
    }

    @Override
    public List<GeneralShopNode> getShops(Model model) {
        return List.of(new GeneralShopNode(model, 1, 4),
                new WeaponShopNode(model, 4, 2));
    }
}
