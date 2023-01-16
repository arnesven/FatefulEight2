package model.map.locations;

import model.Model;
import model.map.CastleLocation;
import model.states.dailyaction.GeneralShopNode;
import model.states.dailyaction.GrandEmporium;
import model.states.dailyaction.RoyalArmory;
import view.MyColors;

import java.util.List;

public class SunblazeCastle extends CastleLocation {
    public SunblazeCastle() {
        super("Sunblaze Castle", MyColors.YELLOW, "Prince Elozi");
    }

    @Override
    public List<GeneralShopNode> getShops(Model model) {
        return List.of(new GrandEmporium(model, 1, 3), new RoyalArmory(model, 5, 2));
    }
}
