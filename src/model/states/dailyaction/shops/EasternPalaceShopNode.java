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
    public static final Sprite WALL = new Sprite32x32("lowerwall", "world_foreground.png", 0x24,
            MyColors.DARK_GRAY, MyColors.WHITE, MyColors.TAN);
    public static final Sprite DOOR = new Sprite32x32("door", "world_foreground.png", 0x34,
            MyColors.DARK_GRAY, MyColors.WHITE, MyColors.TAN, MyColors.DARK_RED);
    public static final Sprite OVER_DOOR = new Sprite32x32("overdoor", "world_foreground.png", 0x06,
            MyColors.DARK_GRAY, MyColors.WHITE, MyColors.TAN, MyColors.BLACK);

    public EasternPalaceShopNode(Model model, int col, int row) {
        super(model, col, row);
    }

    @Override
    public Sprite getLowerWallSprite() {
        return WALL;
    }

    @Override
    public Sprite getDoorSprite() {
        return DOOR;
    }

    @Override
    public Sprite getOverDoorSprite() {
        return OVER_DOOR;
    }

    @Override
    public Sprite getBackgroundSprite() {
        return SPRITE;
    }
}
