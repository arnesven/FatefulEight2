package model.items.clothing;

import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class LeatherTunic extends Clothing {
    private static final Sprite SPRITE = new ItemSprite(15, 2, MyColors.DARK_RED, MyColors.GOLD);

    public LeatherTunic() {
        super("Leather Tunic", 14, 1, false);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new LeatherTunic();
    }
}
