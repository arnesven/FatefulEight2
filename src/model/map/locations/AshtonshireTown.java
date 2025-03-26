package model.map.locations;

import model.Model;
import model.map.TownLocation;
import model.states.dailyaction.shops.GeneralShopNode;
import model.states.dailyaction.shops.MagicShopNode;

import java.awt.*;
import java.util.List;

public class AshtonshireTown extends TownLocation {
    public static final String NAME = "Ashtonshire";

    public AshtonshireTown() {
        super(NAME, "Sheriff Alderborne", false);
    }

    @Override
    public Point getTavernPosition() {
        Point p = super.getTavernPosition();
        p.x++;
        p.y -= 2;
        return p;
    }

    @Override
    public List<GeneralShopNode> getShops(Model model) {
        return List.of(new GeneralShopNode(model, 1, 2),
                new MagicShopNode(model, 1, 4));
    }

    @Override
    public boolean getLordGender() {
        return false;
    }

    @Override
    public String getGeographicalDescription() {
        return "It's a village on the edge of the Eastern Woodland.";
    }
}
