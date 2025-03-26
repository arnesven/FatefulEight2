package model.map.locations;

import model.Model;
import model.map.TownLocation;
import model.states.dailyaction.shops.GeneralShopNode;
import model.states.dailyaction.shops.MagicShopNode;

import java.awt.*;
import java.util.List;

public class SouthMeadhomeTown extends TownLocation {
    public static final String NAME = "South Meadhome";

    public SouthMeadhomeTown() {
        super(NAME, "Mayor Calhoun", true);
    }

    @Override
    public List<GeneralShopNode> getShops(Model model) {
        return List.of(new MagicShopNode(model, 3, 6), new GeneralShopNode(model, 4, 6));
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
