package model.states.dailyaction.shops;

import model.Model;
import view.sprites.Sprite;

public class AncientCityMagicShop extends MagicShopNode {
    public AncientCityMagicShop(Model model, int col, int row) {
        super(model, col, row);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return AncientCityShop.SPRITE;
    }
}
