package model.map.locations;

import model.Model;
import model.map.CastleLocation;
import model.races.Race;
import model.states.dailyaction.GeneralShopNode;
import model.states.dailyaction.MagicSuperShop;
import model.states.dailyaction.RoyalArmory;
import view.MyColors;

import java.awt.*;
import java.util.ArrayList;
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

    @Override
    public List<Point> getExtraKingdomPositions() {
        List<Point> result = new ArrayList<>();
        for (int y = 19; y < 27; ++y) {
            for (int x = 26; x < 30; ++x) {
                if (x != 29 || y != 24) {
                    result.add(new Point(x, y));
                }
            }
        }
        // Temple of the Surf
        result.addAll(List.of(new Point(24, 24), new Point(25, 24),
                new Point(24, 25), new Point(25, 25), new Point(25, 26)));
        // Desert of Ronk
        result.addAll(List.of(new Point(33, 20),
                new Point(34, 19), new Point(36, 19), new Point(37, 20),
                new Point(37, 19), new Point(38, 19), new Point(38, 20)));
        // Lower Theln area
        result.addAll(List.of(new Point(26, 16), new Point(26, 17),
                new Point(25, 17), new Point(27, 18),
                new Point(27, 17), new Point(28, 17),
                new Point(26, 18), new Point(28, 18)));
        result.addAll(List.of(new Point(31, 21), new Point(32, 20),
                new Point(30, 20), new Point(30, 21),
                new Point(30, 18), new Point(30, 19),
                new Point(31, 19), new Point(31, 20)));
        return result;
    }
}
