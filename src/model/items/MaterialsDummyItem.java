package model.items;

import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class MaterialsDummyItem extends InventoryDummyItem {

    private static final Sprite SPRITE = new ItemSprite(3, 12, MyColors.LIGHT_GRAY,
            MyColors.BROWN, MyColors.CYAN);
    private final int amount;

    public MaterialsDummyItem(int materials) {
        super("Materials (" + materials + ")", 0);
        this.amount = materials;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getWeight() {
        return amount * Inventory.WEIGHT_OF_MATERIALS;
    }

    @Override
    public String getShoppingDetails() {
        return ", Raw materials with which you can craft various items.";
    }
}
