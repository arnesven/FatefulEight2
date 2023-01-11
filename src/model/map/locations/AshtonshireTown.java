package model.map.locations;

import model.Model;
import model.map.TownLocation;
import model.states.dailyaction.GeneralShopNode;
import model.states.dailyaction.MagicShopNode;

import java.awt.*;
import java.util.List;

public class AshtonshireTown extends TownLocation {
    public AshtonshireTown() {
        super("Ashtonshire", "Sheriff Alderborne", false);
    }

    @Override
    public Point getTavernPosition() {
        Point p = super.getTavernPosition();
        p.x++;
        p.y--;
        return p;
    }

    @Override
    public List<GeneralShopNode> getShops(Model model) {
        return List.of(new GeneralShopNode(model, 1, 2),
                new MagicShopNode(model, 1, 4));
    }
}
