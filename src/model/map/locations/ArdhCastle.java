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

public class ArdhCastle extends CastleLocation {
    public static final String CASTLE_NAME = "Castle Ardh";

    public ArdhCastle() {
        super(CASTLE_NAME, MyColors.BLUE, "Count Aldeck", Race.HIGH_ELF);
    }

    @Override
    public Point getTavernPosition() {
        return new Point(6, 5);
    }

    @Override
    public List<GeneralShopNode> getShops(Model model) {
        return List.of(new RoyalArmory(model, 1, 3), new MagicSuperShop(model, 5, 2));
    }

    @Override
    public boolean getLordGender() {
        return false;
    }


    @Override
    public Point getLeaguePosition() {
        return new Point(6, 2);
    }

    @Override
    public Point getClubPosition() {
        return new Point(6, 1);
    }
}
