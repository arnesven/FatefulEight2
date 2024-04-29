package model.items;

import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class ObolsDummyItem extends InventoryDummyItem {
    private static final Sprite SPRITE = new ItemSprite(2, 12, MyColors.LIGHT_GRAY,
            MyColors.GRAY_RED, MyColors.CYAN);
    private final int amount;

    public ObolsDummyItem(int obols) {
        super("Obols (" + obols + ")", obols/10);
        this.amount = obols;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public String getShoppingDetails() {
        return ", A sub-currency mostly used for gambling purposes.";
    }

    @Override
    public Item copy() {
        return new ObolsDummyItem(amount);
    }

    @Override
    public int getWeight() {
        return 0;
    }

    @Override
    public void addYourself(Inventory inventory) {
        inventory.setObols(inventory.getObols() + amount);
    }

    @Override
    public int getSellValue() {
        return getCost();
    }
}
