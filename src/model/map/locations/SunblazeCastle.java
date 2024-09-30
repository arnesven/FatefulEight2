package model.map.locations;

import model.Model;
import model.map.CastleLocation;
import model.races.Race;
import model.states.dailyaction.GeneralShopNode;
import model.states.dailyaction.GrandEmporium;
import model.states.dailyaction.RoyalArmory;
import view.MyColors;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SunblazeCastle extends CastleLocation {
    public static final String CASTLE_NAME = "Sunblaze Castle";

    public SunblazeCastle() {
        super(CASTLE_NAME, MyColors.YELLOW, "Prince Elozi", Race.SOUTHERN_HUMAN);
    }

    @Override
    public List<GeneralShopNode> getShops(Model model) {
        return List.of(new GrandEmporium(model, 1, 3), new RoyalArmory(model, 5, 2));
    }

    @Override
    public boolean getLordGender() {
        return false;
    }

    @Override
    public Point getLeaguePosition() {
        return new Point(1, 1);
    }

    @Override
    public Point getClubPosition() {
        return new Point(6, 1);
    }

    @Override
    public List<Point> getExtraKingdomPositions() {
        List<Point> result = new ArrayList<>();
        // South Western peninsula
        for (int y = 25; y < 28; ++y) {
            for (int x = 2; x < 11; ++x) {
                if (x != 4 || y != 25) {
                    result.add(new Point(x, y));
                }
            }
        }
        // Temple of the Plains
        for (int y = 19; y < 22; ++y) {
            for (int x = 12; x < 18; ++x) {
                result.add(new Point(x, y));
            }
        }
        result.addAll(List.of(new Point(14, 18), new Point(16, 18)));

        // Western Coast
        result.addAll(List.of(new Point(7, 24), new Point(8, 24),
                new Point(9, 24), new Point(10, 24),
                new Point(8, 23), new Point(9, 23), new Point(10, 23),
                new Point(11, 23), new Point(10, 22), new Point(11, 22),
                new Point(12, 22), new Point(13, 22)));

        // East of the Mountains
        result.addAll(List.of(new Point(20, 22), new Point(21, 23),
                new Point(22, 23), new Point(23, 24),
                new Point(22, 24), new Point(23, 25), new Point(22, 25),
                new Point(23, 26)));

        return result;
    }
}
