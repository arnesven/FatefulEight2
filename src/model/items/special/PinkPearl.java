package model.items.special;

import model.items.Item;
import view.MyColors;
import view.sprites.Sprite;

public class PinkPearl extends PearlItem {
    private static final Sprite SPRITE = new PearlSprite(MyColors.PINK, MyColors.LIGHT_RED, MyColors.LIGHT_PINK);

    public PinkPearl() {
        super("Pink Pearl", 0);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return null;
    }
}
