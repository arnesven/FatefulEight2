package model.map.locations;

import model.Model;
import model.map.TownLocation;
import model.states.dailyaction.shops.GeneralShopNode;
import model.states.dailyaction.shops.WeaponShopNode;

import java.awt.*;
import java.util.List;

public class RoukonTown extends TownLocation {
    public static final String NAME = "Roukon";

    public RoukonTown() {
        super(NAME, "Mayor Stephens", true);
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

    @Override
    public int charterBoatEveryNDays() {
        return 2;
    }

    @Override
    public Point getCareerOfficePosition() {
        return new Point(5, 3);
    }

    @Override
    public String getGeographicalDescription() {
        return "It lies on the southern shores of the Great Strait.";
    }
}
