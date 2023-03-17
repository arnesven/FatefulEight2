package model.map.locations;

import model.Model;
import model.map.CastleLocation;
import model.states.dailyaction.GeneralShopNode;
import model.states.dailyaction.MagicSuperShop;
import model.states.dailyaction.RoyalArmory;
import view.MyColors;

import java.awt.*;
import java.util.List;

public class BogdownCastle extends CastleLocation {
    public BogdownCastle() {
        super("Bogdown Castle", MyColors.DARK_GREEN, "King Burod");
    }

    @Override
    public Point getTavernPosition() {
        return new Point(1, 2);
    }

    @Override
    public java.util.List<GeneralShopNode> getShops(Model model) {
        return List.of(new RoyalArmory(model, 1, 5), new MagicSuperShop(model, 5, 1));
    }

    @Override
    public boolean getLordGender() {
        return false;
    }
}
