package model.items.special;

import model.items.Item;
import view.MyColors;
import view.sprites.Sprite;

public class BlackPearl extends PearlItem {
    private static final Sprite SPRITE =
            new PearlSprite(MyColors.BLACK, MyColors.DARK_BLUE, MyColors.WHITE);

    public BlackPearl() {
        super("Black Pearl", 0);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new BlackPearl();
    }
}
