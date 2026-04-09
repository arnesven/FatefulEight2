package model.items.treasures;

import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class TopazTreasureItem extends GemstoneItem {
    private static final Sprite SPRITE = new ItemSprite(9, 17, MyColors.GOLD, MyColors.YELLOW,
            MyColors.DARK_GRAY);

    public TopazTreasureItem() {
        super("Topaz", 50);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new TopazTreasureItem();
    }
}
