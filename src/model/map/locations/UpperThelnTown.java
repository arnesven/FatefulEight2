package model.map.locations;

import model.Model;
import model.map.TownLocation;
import model.map.WaterLocation;
import model.states.dailyaction.shops.GeneralShopNode;
import model.states.dailyaction.shops.WeaponShopNode;

import java.awt.*;
import java.util.List;

public class UpperThelnTown extends TownLocation {
    public static final String NAME = "Upper Theln";

    public UpperThelnTown() {
        super(NAME, "Mayor Rutherford", WaterLocation.riverside);
    }

    @Override
    public List<GeneralShopNode> getShops(Model model) {
        return List.of(new GeneralShopNode(model, 4, 5),
                new WeaponShopNode(model, 6, 1));
    }

    @Override
    public boolean getLordGender() {
        return true;
    }

    @Override
    public Point getTavernPosition() {
        Point p = super.getTavernPosition();
        p.y--;
        return p;
    }

    @Override
    public Point getCareerOfficePosition() {
        return new Point(2, 5);
    }

    @Override
    public boolean bothBoatAndCarriage() {
        return true;
    }

    @Override
    public List<String> getSeaTravelRoutes() {
        return List.of("Lower Theln.........Often",
                       "Cape Paxton........Rarely",
                       "Chartered Boats.....Never");
    }

    @Override
    public String getGeographicalDescription() {
        return "It's on the river Thelnius, surrounded by vast farmlands.";
    }
}
