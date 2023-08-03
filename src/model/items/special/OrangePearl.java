package model.items.special;

import model.items.Item;
import view.MyColors;
import view.sprites.Sprite;

public class OrangePearl extends PearlItem {
    private static final Sprite SPRITE =
            new PearlSprite(MyColors.ORANGE, MyColors.RED, MyColors.YELLOW);

    public OrangePearl() {
        super("Orange Pearl", 0);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new OrangePearl();
    }
}
