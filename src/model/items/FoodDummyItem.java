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
        return ", Rations which can be consumed by the members of your party. Each party " +
                "member consumes 1 ration per day unless they are fed in any other way.";
    }
}
