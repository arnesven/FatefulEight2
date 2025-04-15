package model.states.dailyaction.shops;

import model.Model;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.TownSubView;

public class EasternPalaceShopNode extends GeneralShopNode {
    private static final Sprite SPRITE = new Sprite32x32("shopping", "world_foreground.png", 0x22,
                                           MyColors.YELLOW, TownSubView.PATH_COLOR, MyColors.RED, MyColors.WHITE);

    public EasternPalaceShopNode(Model model, int col, int row) {
        super(model, col, row);
    }

    @Override
    public Sprite getBackgroundSprite() {
        return SPRITE;
    }
}
