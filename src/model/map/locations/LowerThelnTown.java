package model.map.locations;

import model.Model;
import model.map.TownLocation;
import model.states.dailyaction.shops.ArmorShopNode;
import model.states.dailyaction.shops.GeneralShopNode;

import java.awt.*;
import java.util.List;

public class LowerThelnTown extends TownLocation {
    public static final String NAME = "Lower Theln";

    public LowerThelnTown() {
        super(NAME, "Mayor Engels", true);
    }

    @Override
    public List<GeneralShopNode> getShops(Model model) {
        return List.of(new GeneralShopNode(model, 1, 1),
                new ArmorShopNode(model, 5, 5));
    }

    @Override
    public boolean getLordGender() {
        return false;
    }

    @Override
    public int charterBoatEveryNDays() {
        return 8;
    }

    @Override
    public Point getCareerOfficePosition() {
        return new Point(5, 2);
    }

    @Override
    public List<String> getSeaTravelRoutes() {
        return List.of("East Durham........Rarely",
                       "Cape Paxton........Rarely",
                       "Roukon.............Rarely",
                       "Ebonshire.......Sometimes",
                       "Upper Theln.........Often",
                       "Little Erinde......Rarely",
                       "Chartered Boats....Rarely"); // Rarely = 6+)
    }

    @Override
    public String getGeographicalDescription() {
        return "It is located right where the river Thelnius meets the sea.";
    }
}
