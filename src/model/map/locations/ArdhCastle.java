package model.map.locations;

import model.Model;
import model.map.CastleLocation;
import model.states.dailyaction.GeneralShopNode;
import model.states.dailyaction.MagicSuperShop;
import model.states.dailyaction.RoyalArmory;
import view.MyColors;

import java.awt.*;
import java.util.List;

public class ArdhCastle extends CastleLocation {
    public ArdhCastle() {
        super("Castle Ardh", MyColors.BLUE, "Count Aldeck");
    }

    @Override
    public Point getTavernPosition() {
        return new Point(6, 6);
    }

    @Override
    public List<GeneralShopNode> getShops(Model model) {
        return List.of(new RoyalArmory(model, 1, 3), new MagicSuperShop(model, 5, 2));
    }
}
