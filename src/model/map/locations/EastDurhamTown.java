package model.map.locations;

import model.Model;
import model.map.TownLocation;
import model.states.dailyaction.shops.ArmorShopNode;
import model.states.dailyaction.shops.GeneralShopNode;

import java.awt.*;
import java.util.List;

public class EastDurhamTown extends TownLocation {
    public static final String NAME = "East Durham";

    public EastDurhamTown() {
        super(NAME, "Mayor Bison", true);
    }

    @Override
    public Point getTavernPosition() {
        Point p = super.getTavernPosition();
        p.x--;
        p.y++;
        return p;
    }

    @Override
    public List<GeneralShopNode> getShops(Model model) {
        return List.of(new GeneralShopNode(model, 1, 3), new ArmorShopNode(model, 5, 3));
    }

    @Override
    public boolean getLordGender() {
        return false;
    }

    @Override
    public int charterBoatEveryNDays() {
        return 3;
    }

    @Override
    public String getGeographicalDescription() {
        return "It's on the western shore of the Icy Bay.";
    }
}
