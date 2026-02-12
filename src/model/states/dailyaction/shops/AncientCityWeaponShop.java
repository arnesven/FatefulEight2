package model.states.dailyaction.shops;

import model.Model;
import view.sprites.Sprite;

public class AncientCityWeaponShop extends WeaponShopNode {
    public AncientCityWeaponShop(Model model, int col, int row) {
        super(model, col, row);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return AncientCityShop.SPRITE;
    }
}
