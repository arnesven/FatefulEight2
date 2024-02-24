package model.items;

import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class GoldDummyItem extends InventoryDummyItem {
    private static final Sprite SPRITE = new ItemSprite(1, 12, MyColors.ORANGE,
            MyColors.GOLD, MyColors.DARK_RED);

    public GoldDummyItem(int gold) {
        super("Gold (" + gold + ")", gold);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public String getShoppingDetails() {
        return ", The common currency of all realms.";
    }

    @Override
    public int getWeight() {
        return 0;
    }

    @Override
    public void addYourself(Inventory inventory) {
        inventory.setGold(inventory.getGold() + getCost());
    }
}
