package model.map.locations;

import model.Model;
import model.map.TownLocation;
import model.states.dailyaction.ArmorShopNode;
import model.states.dailyaction.GeneralShopNode;

import java.awt.*;
import java.util.List;

public class LittleErindeTown extends TownLocation {
    public LittleErindeTown() {
        super("Little Erinde", "Mayor Gorda", true);
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
}
