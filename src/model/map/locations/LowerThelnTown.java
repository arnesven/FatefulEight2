package model.map.locations;

import model.Model;
import model.map.TownLocation;
import model.states.dailyaction.ArmorShopNode;
import model.states.dailyaction.GeneralShopNode;

import java.util.List;

public class LowerThelnTown extends TownLocation {
    public LowerThelnTown() {
        super("Lower Theln", "Mayor Engels", true);
    }

    @Override
    public List<GeneralShopNode> getShops(Model model) {
        return List.of(new GeneralShopNode(model, 1, 1),
                new ArmorShopNode(model, 5, 5));
    }
}
