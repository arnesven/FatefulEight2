package model.map.locations;

import model.Model;
import model.map.TownLocation;
import model.states.dailyaction.GeneralShopNode;
import model.states.dailyaction.WeaponShopNode;

import java.util.List;

public class UpperThelnTown extends TownLocation {
    public UpperThelnTown() {
        super("Upper Theln", "Mayor Rutherford", true);
    }

    @Override
    protected List<GeneralShopNode> getShops(Model model) {
        return List.of(new GeneralShopNode(model, 4, 5),
                new WeaponShopNode(model, 6, 1));
    }
}
