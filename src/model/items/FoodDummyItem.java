package model.items;

import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class FoodDummyItem extends InventoryDummyItem {
    private static final Sprite SPRITE = new ItemSprite(0, 12, MyColors.BEIGE,
            MyColors.GOLD, MyColors.DARK_RED);
    private final int amount;

    public FoodDummyItem(int food) {
        super("Food (" + food + ")", food/5);
        this.amount = food;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public String getShoppingDetails() {
        return ", Each party " +
                "member consumes 1 ration per day unless they are fed in any other way.";
    }

    @Override
    public Item copy() {
        return new FoodDummyItem(amount);
    }

    @Override
    public int getWeight() {
        return amount * Inventory.WEIGHT_OF_FOOD;
    }

    @Override
    public void addYourself(Inventory inventory) {
        inventory.setFood(inventory.getFood() + amount);
    }

    @Override
    public String getDescription() {
        return amount + " rations";
    }
}
