package model.map.locations;

import model.Model;
import model.map.CastleLocation;
import model.races.Race;
import model.states.dailyaction.GeneralShopNode;
import model.states.dailyaction.GrandEmporium;
import model.states.dailyaction.RoyalArmory;
import view.MyColors;

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
}
