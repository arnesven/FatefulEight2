package model.items.treasures;

import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class EmeraldTreasureItem extends GemstoneItem {

    private static final Sprite SPRITE = new ItemSprite(6, 17, MyColors.DARK_GREEN, MyColors.GREEN,
            MyColors.DARK_GRAY);

    public EmeraldTreasureItem() {
        super("Emerald", 250);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new EmeraldTreasureItem();
    }
}
