package model.map.locations;

import model.Model;
import model.map.TownLocation;
import model.map.WaterLocation;
import model.states.dailyaction.shops.AccessoryShopNode;
import model.states.dailyaction.shops.GeneralShopNode;

import java.awt.*;
import java.util.List;

public class SaintQuellinTown extends TownLocation {
    public static final String NAME = "Saint Quellin";

    public SaintQuellinTown() {
        super(NAME, "Councilman Egon", WaterLocation.inland);
    }

    @Override
    public List<GeneralShopNode> getShops(Model model) {
        return List.of(new GeneralShopNode(model, 1, 5),
                new AccessoryShopNode(model, 1, 3));
    }

    @Override
    public boolean getLordGender() {
        return false;
    }

    @Override
    public Point getTavernPosition() {
        return new Point(5, 5);
    }

    @Override
    public Point getCareerOfficePosition() {
        return new Point(2, 5);
    }

    @Override
    public List<Point> getDecorativeHousePositions() {
        return TownLocation.convertToPositions(
                ".X......" +
                "........" +
                "X......X" +
                "......X." +
                "........" +
                "..X....." +
                ".....XX.");
    }

    @Override
    public String getGeographicalDescription() {
        return "It lies just south of the desert of Zind.";
    }
}
