package model.map.locations;

import model.Model;
import model.map.CastleLocation;
import model.races.Race;
import model.states.dailyaction.shops.GeneralShopNode;
import model.states.dailyaction.shops.GrandEmporium;
import model.states.dailyaction.shops.RoyalArmory;
import view.MyColors;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class ArkvaleCastle extends CastleLocation {

    public static final String CASTLE_NAME = "Arkvale Castle";

    public ArkvaleCastle() {
        super(CASTLE_NAME, MyColors.WHITE, "Queen Valstine", Race.NORTHERN_HUMAN);
    }

    @Override
    public Point getTavernPosition() {
        return new Point(5, 2);
    }


    @Override
    public java.util.List<GeneralShopNode> getShops(Model model) {
        return List.of(new RoyalArmory(model, 6, 1), new GrandEmporium(model, 2, 4));
    }

    @Override
    public boolean getLordGender() {
        return true;
    }

    @Override
    public String getGeographicalDescription() {
        return "It's up in the Northern Mountains.";
    }


    @Override
    public Point getLeaguePosition() {
        return new Point(1, 1);
    }

    @Override
    public Point getClubPosition() {
        return new Point(1, 2);
    }

    @Override
    public List<Point> getExtraKingdomPositions() {
        List<Point> result = new ArrayList<>();
        for (int y = 8; y < 17; ++y) {
            for (int x = 28; x < 31; ++x) {
                result.add(new Point(x, y));
            }
        }
        for (int y = 17; y < 19; ++y) {
            for (int x = 29; x < 39; ++x) {
                if (x != 30 || y != 18) {
                    result.add(new Point(x, y));
                }
            }
        }
        // Ashtonshire and hills west of.
        result.addAll(List.of(new Point(31, 15), new Point(32, 15),
                new Point(33, 16), new Point(31, 16), new Point(32, 16),
                new Point(34, 16)));
        // Mountains south of the sea
        result.addAll(List.of(new Point(38, 16), new Point(39, 16)));
        // Cape Paxton and Waterfront Inn
        result.addAll(List.of(new Point(25, 10), new Point(26, 9),
                new Point(27, 9), new Point(26, 10), new Point(27, 10),
                new Point(25, 11), new Point(26, 11), new Point(27, 11),
                new Point(26, 12), new Point(27, 12), new Point(25, 13),
                new Point(26, 13), new Point(27, 13), new Point(27, 14),
                new Point(27, 15)));
        // Mountains north of Desert of Ronk
        result.addAll(List.of(new Point(32, 19), new Point(33, 19), new Point(35, 19)));
        return result;
    }
}
