package model.map.locations;

import model.Model;
import model.map.TownLocation;
import model.states.dailyaction.GeneralShopNode;
import model.states.dailyaction.WeaponShopNode;

import java.awt.*;
import java.util.List;

public class RoukonTown extends TownLocation {
    public RoukonTown() {
        super("Roukon", "Mayor Stephens", true);
    }

    @Override
    public Point getTavernPosition() {
        return new Point(5, 4);
    }

    @Override
    public List<GeneralShopNode> getShops(Model model) {
        return List.of(new WeaponShopNode(model, 1, 4), new GeneralShopNode(model, 3, 6));
    }

    @Override
    public boolean getLordGender() {
        return true;
    }
}
