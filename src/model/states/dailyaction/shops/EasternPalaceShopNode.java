package model.states.dailyaction.shops;

import model.Model;
import view.MyColors;
import view.sprites.MiniItemSprite;
import view.sprites.Sprite;
import view.sprites.Sprite32x32;
import view.subviews.TownSubView;

public class EasternPalaceShopNode extends GeneralShopNode {
    private static final Sprite SPRITE = new Sprite32x32("shopping", "world_foreground.png", 0x22,
                                           MyColors.YELLOW, TownSubView.PATH_COLOR, MyColors.RED, MyColors.WHITE);

    private static final Sprite[] SHOP_DECORATIONS = new Sprite[]{
            GENERAL_SHOP_SIGN,
            new MiniItemSprite(0, MyColors.LIGHT_GRAY, MyColors.BROWN),
            new MiniItemSprite(0, MyColors.GOLD, MyColors.BLUE),
    };

    public EasternPalaceShopNode(Model model, int col, int row) {
        super(model, col, row);
    }

    @Override
    protected Sprite[] getShopDecorations() {
        return SHOP_DECORATIONS;
    }

    @Override
    public Sprite getBackgroundSprite() {
        return SPRITE;
    }
}
