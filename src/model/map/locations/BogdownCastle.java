package model.map.locations;

import model.Model;
import model.map.CastleLocation;
import model.races.Race;
import model.states.dailyaction.shops.GeneralShopNode;
import model.states.dailyaction.shops.MagicSuperShop;
import model.states.dailyaction.shops.RoyalArmory;
import view.MyColors;

import java.awt.*;
import java.util.ArrayList;
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
    public String getGeographicalDescription() {
        return "It's a castle surrounded by swamps, and to the south is the Great Strait.";
    }

    @Override
    public Point getLeaguePosition() {
        return new Point(5, 1);
    }

    @Override
    public Point getClubPosition() {
        return new Point(1, 4);
    }

    @Override
    public List<Point> getExtraKingdomPositions() {
        List<Point> result = new ArrayList<>();
        for (int y = 11; y < 15; ++y) {
            for (int x = 8; x < 10; ++x) {
                result.add(new Point(x, y));
            }
        }
        result.addAll(List.of(new Point(7, 12), new Point(7, 14),
                new Point(9, 15), new Point(10, 17), new Point(11, 17),
                new Point(11, 18), new Point(12, 17), new Point(12, 18),
                new Point(13, 18)));

        // Mainland chunk
        for (int y = 18; y < 22; ++y) {
            for (int x = 18; x < 26; ++x) {
                result.add(new Point(x, y));
            }
        }

        // Northern coast
        for (int x = 18; x < 25; ++x) {
            if (x != 23) {
                result.add(new Point(x, 17));
            }
        }

        // Ackerville
        for (int x = 21; x < 26; ++x) {
            result.add(new Point(x, 22));
            if (x > 23) {
                result.add(new Point(x, 23));
            }
        }

        // East Durham and north of.
        result.addAll(List.of(new Point(21, 13), new Point(22, 12),
                new Point(21, 12), new Point(21, 11), new Point(21, 10),
                new Point(20, 9), new Point(21, 9), new Point(22, 8)));
        // Monastary Island
        result.addAll(List.of(new Point(22, 15), new Point(23, 15)));
        return result;
    }
}
