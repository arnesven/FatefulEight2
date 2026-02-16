package model.map.locations;

import model.Model;
import model.map.TownLocation;
import model.map.WaterLocation;
import model.states.dailyaction.shops.GeneralShopNode;
import model.states.dailyaction.shops.MagicShopNode;

import java.awt.*;
import java.util.List;

public class SouthMeadhomeTown extends TownLocation {
    public static final String NAME = "South Meadhome";

    public SouthMeadhomeTown() {
        super(NAME, "Mayor Calhoun", WaterLocation.riverside);
    }

    @Override
    public List<GeneralShopNode> getShops(Model model) {
        return List.of(new MagicShopNode(model, 3, 6), new GeneralShopNode(model, 4, 6));
    }

    @Override
    public List<Point> getDecorativeHousePositions() {
        return List.of(new Point(6, 1), // TODO: Do this for the other towns and castles as well.
            new Point(1, 2), new Point(3, 2),
            new Point(0, 3), new Point(1, 3),
            new Point(6, 5),
            new Point(5, 6), new Point(6, 6),
            new Point(0, 7), new Point(2, 7),
            new Point(5, 7), new Point(6, 7));
    }

    @Override
    public boolean getLordGender() {
        return true;
    }

    @Override
    public Point getTavernPosition() {
        return new Point(2, 1);
    }

    @Override
    public boolean noBoat() {
        return true;
    }

    @Override
    public boolean bothBoatAndCarriage() {
        return true;
    }

    @Override
    public String getGeographicalDescription() {
        return "It is located south of Great Forest, not far from the shores of the Southern Sea.";
    }
}
