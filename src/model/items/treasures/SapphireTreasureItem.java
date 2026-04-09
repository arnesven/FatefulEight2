package model.items.treasures;

import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class SapphireTreasureItem extends GemstoneItem {

    private static final Sprite SPRITE = new ItemSprite(6, 17, MyColors.DARK_BLUE, MyColors.BLUE,
            MyColors.DARK_GRAY);

    public SapphireTreasureItem() {
        super("Sapphire", 100);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new SapphireTreasureItem();
    }
}
