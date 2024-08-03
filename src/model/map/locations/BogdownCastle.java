package model.map.locations;

import model.Model;
import model.map.CastleLocation;
import model.races.Race;
import model.states.dailyaction.GeneralShopNode;
import model.states.dailyaction.MagicSuperShop;
import model.states.dailyaction.RoyalArmory;
import view.MyColors;

import java.awt.*;
import java.util.List;

public class BogdownCastle extends CastleLocation {
    public static final String CASTLE_NAME = "Bogdown Castle";

    public BogdownCastle() {
        super(CASTLE_NAME, MyColors.DARK_GREEN, "King Burod", Race.WOOD_ELF);
    }

    @Override
    public Point getTavernPosition() {
        return new Point(1, 2);
    }

    @Override
    public java.util.List<GeneralShopNode> getShops(Model model) {
        return List.of(new RoyalArmory(model, 1, 5), new MagicSuperShop(model, 6, 1));
    }

    @Override
    public boolean getLordGender() {
        return false;
    }

    @Override
    public Point getLeaguePosition() {
        return new Point(5, 1);
    }
}
