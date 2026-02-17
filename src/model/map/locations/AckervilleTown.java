package model.map.locations;

import model.Model;
import model.map.MonsterHuntData;
import model.map.TownLocation;
import model.map.WaterLocation;
import model.states.dailyaction.shops.GeneralShopNode;
import model.states.dailyaction.shops.WeaponShopNode;

import java.awt.*;
import java.util.List;

public class AckervilleTown extends TownLocation {
    public static final String NAME = "Ackerville";

    public AckervilleTown() {
        super(NAME, "Elder Treya", WaterLocation.coastal);
    }

    @Override
    public Point getTavernPosition() {
        return new Point(6, 1);
    }

    @Override
    public List<GeneralShopNode> getShops(Model model) {
        return List.of(new WeaponShopNode(model, 1, 3),
                new GeneralShopNode(model, 5, 4));
    }

    @Override
    public List<String> getSeaTravelRoutes() {
        return List.of("Little Erinde...Sometimes",
                       "Ebonshire.......Sometimes",
                       "Chartered Boats.....Never");
    }

    @Override
    public boolean getLordGender() {
        return true;
    }

    @Override
    public List<Point> getDecorativeHousePositions() {
        return TownLocation.convertToPositions(
          "..X....." +
                ".X.XX..." +
                ".....X.." +
                "XX.....X" +
                "........" +
                "XX.....X" +
                "..X.....");
    }

    @Override
    public String getGeographicalDescription() {
        return "It's close to the swamps of the Great Forest, on the shores of Lake Acker.";
    }
}
