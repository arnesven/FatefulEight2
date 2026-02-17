package model.map.locations;

import model.Model;
import model.map.TownLocation;
import model.map.WaterLocation;
import model.states.dailyaction.shops.GeneralShopNode;
import model.states.dailyaction.shops.MagicShopNode;

import java.awt.*;
import java.util.List;

public class SheffieldTown extends TownLocation {
    public static final String NAME = "Sheffield";

    public SheffieldTown() {
        super(NAME, "Mayor Jorgensen", WaterLocation.inland);
    }

    @Override
    public Point getTavernPosition() {
        return new Point(5, 2);
    }

    @Override
    public List<GeneralShopNode> getShops(Model model) {
        return List.of(new GeneralShopNode(model, 5, 5),
                new MagicShopNode(model, 1, 4));
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
    public List<Point> getDecorativeHousePositions() {
        return TownLocation.convertToPositions(
          "....X..." +
                      "X..X...." +
                      ".X.....X" +
                      "........" +
                      "........" +
                      "...XX..." +
                      "..X.....");
    }

    @Override
    public String getGeographicalDescription() {
        return "It lies just west of Castle Ardh";
    }
}
