package model.items.special;

import model.items.Item;
import view.MyColors;
import view.sprites.Sprite;

public class GreenPearl extends PearlItem {
    private static final Sprite SPRITE =
            new PearlSprite(MyColors.GREEN, MyColors.DARK_GREEN, MyColors.LIGHT_GREEN);

    public GreenPearl() {
        super("Green Pearl", 0);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new GreenPearl();
    }
}
