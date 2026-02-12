package model.states.dailyaction.shops;

import model.Model;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;

public class AncientCityShop extends GeneralShopNode {

    public static final Sprite SPRITE = new Sprite32x32("cityshop", "world_foreground.png", 0xF6,
            MyColors.BLACK, MyColors.PEACH, MyColors.DARK_RED, MyColors.BROWN);

    public AncientCityShop(Model model, int col, int row) {
        super(model, col, row);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return SPRITE;
    }
}
