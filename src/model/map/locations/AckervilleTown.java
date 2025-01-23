package model.map.locations;

import model.Model;
import model.map.TownLocation;
import model.states.dailyaction.GeneralShopNode;
import model.states.dailyaction.WeaponShopNode;

import java.awt.*;
import java.util.List;

public class AckervilleTown extends TownLocation {
    public static final String NAME = "Ackerville";

    public AckervilleTown() {
        super(NAME, "Elder Treya", true);
    }

    @Override
    public Point getTavernPosition() {
        return new Point(6, 1);
    }

    @Override
    public List<GeneralShopNode> getShops(Model model) {
        return List.of(new WeaponShopNode(model, 1, 3),
                new GeneralShopNode(model, 5, 4));
    }

    @Override
    public boolean getLordGender() {
        return true;
    }

    @Override
    public String getGeographicalDescription() {
        return "It's close to the swamps of the Great Forest, on the shores of Lake Acker.";
    }
}
