package model.map.locations;

import model.Model;
import model.map.TownLocation;
import model.states.dailyaction.ArmorShopNode;
import model.states.dailyaction.GeneralShopNode;

import java.awt.*;
import java.util.List;

public class BullsVilleTown extends TownLocation {
    public static final String NAME = "Bullsville";

    public BullsVilleTown() {
        super(NAME, "Mayor Oppenberg", false);
    }

    @Override
    public Point getTavernPosition() {
        return new Point(5, 2);
    }

    @Override
    public List<GeneralShopNode> getShops(Model model) {
        return List.of(new ArmorShopNode(model, 3, 7),
                new GeneralShopNode(model, 1, 2));
    }

    @Override
    public boolean getLordGender() {
        return false;
    }

    @Override
    public String getGeographicalDescription() {
        return "It's a farming village east of Castle Ardh.";
    }
}
