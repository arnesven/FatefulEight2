package model.items.special;

import model.items.Item;
import view.MyColors;
import view.sprites.Sprite;

public class PurplePearl extends PearlItem {
    private static final Sprite SPRITE =
            new PearlSprite(MyColors.PURPLE, MyColors.DARK_PURPLE, MyColors.PINK);

    public PurplePearl() {
        super("Purple Pearl", 0);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new PurplePearl();
    }
}
