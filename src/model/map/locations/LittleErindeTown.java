package model.map.locations;

import model.Model;
import model.map.TownLocation;
import model.states.dailyaction.shops.ArmorShopNode;
import model.states.dailyaction.shops.GeneralShopNode;

import java.awt.*;
import java.util.List;

public class LittleErindeTown extends TownLocation {
    public static final String NAME = "Little Erinde";

    public LittleErindeTown() {
        super(NAME, "Mayor Gorda", true);
    }

    @Override
    public Point getTavernPosition() {
        return new Point(4, 6);
    }

    @Override
    public List<GeneralShopNode> getShops(Model model) {
        return List.of(new GeneralShopNode(model, 1, 6),
                new ArmorShopNode(model, 5, 1));
    }

    @Override
    public List<String> getSeaTravelRoutes() {
        return List.of("Ebonshire.......Sometimes",
                       "Ackerville......Sometimes",
                       "Lower Theln........Rarely",
                       "Chartered Boats.....Never");
    }

    @Override
    public boolean getLordGender() {
        return true;
    }

    @Override
    public String getGeographicalDescription() {
        return "It lies to the north of the desert of Drize, on the Ojai River.";
    }
}
