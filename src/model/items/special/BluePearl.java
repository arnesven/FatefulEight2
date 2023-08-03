package model.items.special;

import model.items.Item;
import view.MyColors;
import view.sprites.Sprite;

public class BluePearl extends PearlItem {
    private static final Sprite SPRITE =
            new PearlSprite(MyColors.BLUE, MyColors.DARK_BLUE, MyColors.LIGHT_BLUE);

    public BluePearl() {
        super("Blue Pearl", 0);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new BluePearl();
    }
}
