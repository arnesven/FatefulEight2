package model.items;

import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class IngredientsDummyItem extends InventoryDummyItem {

    private static final Sprite SPRITE = new ItemSprite(14, 7, MyColors.WHITE,
            MyColors.LIGHT_GREEN, MyColors.CYAN);
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
    public int getWeight() {
        return amount * Inventory.WEIGHT_OF_INGREDIENTS;
    }

    @Override
    public void addYourself(Inventory inventory) {
        inventory.addToIngredients(amount);
    }
}
