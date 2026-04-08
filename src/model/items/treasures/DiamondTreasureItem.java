package model.items.treasures;

import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class DiamondTreasureItem extends GemstoneItem {

    private static final Sprite SPRITE = new ItemSprite(7, 17, MyColors.CYAN, MyColors.WHITE,
            MyColors.DARK_GRAY);

    public DiamondTreasureItem() {
        super("Diamond", 1000);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new DiamondTreasureItem();
    }
}
