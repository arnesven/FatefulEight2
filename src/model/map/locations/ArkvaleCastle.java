package model.map.locations;

import model.Model;
import model.map.CastleLocation;
import model.states.dailyaction.GeneralShopNode;
import model.states.dailyaction.GrandEmporium;
import model.states.dailyaction.MagicSuperShop;
import model.states.dailyaction.RoyalArmory;
import view.MyColors;

import java.awt.*;
import java.util.List;

public class ArkvaleCastle extends CastleLocation {
    public ArkvaleCastle() {
        super("Arkvale Castle", MyColors.WHITE, "Queen Valstine");
    }

    @Override
    public Point getTavernPosition() {
        return new Point(5, 2);
    }


    @Override
    public java.util.List<GeneralShopNode> getShops(Model model) {
        return List.of(new RoyalArmory(model, 6, 1), new GrandEmporium(model, 2, 4));
    }
}
