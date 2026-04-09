package model.items.treasures;

import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class AmethystTreasureItem extends GemstoneItem {
    private static final Sprite SPRITE = new ItemSprite(9, 17, MyColors.PURPLE, MyColors.DARK_PURPLE,
            MyColors.DARK_GRAY);;

    public AmethystTreasureItem() {
        super("Amethyst", 25);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new AmethystTreasureItem();
    }
}
