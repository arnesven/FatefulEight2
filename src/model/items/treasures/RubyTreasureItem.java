package model.items.treasures;

import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class RubyTreasureItem extends GemstoneItem {
    private static final Sprite SPRITE = new ItemSprite(6, 17, MyColors.RED, MyColors.DARK_RED,
            MyColors.DARK_GRAY);

    public RubyTreasureItem() {
        super("Ruby", 100);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new RubyTreasureItem();
    }
}
