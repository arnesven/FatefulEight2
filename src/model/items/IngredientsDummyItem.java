package model.items;

import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class IngredientsDummyItem extends InventoryDummyItem {

    public static final Sprite SPRITE = new ItemSprite(12, 12,
            MyColors.GREEN, MyColors.RED, MyColors.BLUE);
    private final int amount;

    public IngredientsDummyItem(int ingredients) {
        super("Ingredients (" + ingredients + ")", 0);
        this.amount = ingredients;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public String getShoppingDetails() {
        return ", Components which you can make alchemical drafts. This includes spices herbs " +
                "tonics minerals and animal parts.";
    }

    @Override
    public Item copy() {
        return new IngredientsDummyItem(amount);
    }

    @Override
    public int getWeight() {
        return amount * Inventory.WEIGHT_OF_INGREDIENTS;
    }

    @Override
    public void addYourself(Inventory inventory) {
        inventory.addToIngredients(amount);
    }

    @Override
    public String getDescription() {
        return amount + " ingredients";
    }
}
