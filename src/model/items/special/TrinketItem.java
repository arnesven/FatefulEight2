package model.items.special;

import model.items.Item;
import model.items.treasures.TreasureItem;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class TrinketItem extends TreasureItem {
    private static final Sprite SPRITE = new ItemSprite(10, 17, MyColors.GOLD, MyColors.LIGHT_GRAY,
            MyColors.DARK_GRAY);

    public TrinketItem() {
        super("Trinket", 20);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getWeight() {
        return 0;
    }

    @Override
    public String getShoppingDetails() {
        return "A useless, but fairly valuable trinket.";
    }

    @Override
    public Item copy() {
        return new TrinketItem();
    }

    @Override
    public boolean isStackable() {
        return true;
    }
}
