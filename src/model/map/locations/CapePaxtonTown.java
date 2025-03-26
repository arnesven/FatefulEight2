package model.map.locations;

import model.Model;
import model.map.TownLocation;
import model.states.dailyaction.shops.GeneralShopNode;
import model.states.dailyaction.shops.WeaponShopNode;

import java.awt.*;
import java.util.List;

public class CapePaxtonTown extends TownLocation {
    public static final String NAME = "Cape Paxton";

    public CapePaxtonTown() {
        super(NAME, "Mayor Dargola", true);
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

    @Override
    public int charterBoatEveryNDays() {
        return 4;
    }

    @Override
    public boolean getLordGender() {
        return true;
    }

    @Override
    public Point getCareerOfficePosition() {
        return new Point(2, 2);
    }

    @Override
    public String getGeographicalDescription() {
        return "It's on the eastern side of the Icy Bay.";
    }
}
