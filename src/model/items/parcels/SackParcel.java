package model.items.parcels;

import model.items.*;
import util.MyRandom;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class SackParcel extends Parcel {
    private static final Sprite SPRITE = new ItemSprite(2, 13, MyColors.BEIGE, MyColors.BROWN, MyColors.TAN);

    private static final int FOOD = 0;
    private static final int INGREDIENTS = 1;
    private static final int MATERIALS = 2;
    private final int contents;
    private final int amount;

    public SackParcel() {
        super("Sack", 4.0);
        this.contents = MyRandom.randInt(3);
        this.amount = MyRandom.randInt(10, 30);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getWeight() {
        if (contents == FOOD) {
            return amount * Inventory.WEIGHT_OF_FOOD;
        }
        if (contents == INGREDIENTS) {
            return amount * Inventory.WEIGHT_OF_INGREDIENTS;
        }
        if (contents == MATERIALS) {
            return amount * Inventory.WEIGHT_OF_MATERIALS;
        }
        return 0;
    }

    @Override
    public String getShoppingDetails() {
        return "A heavy sack filled with unknown contents.";
    }

    @Override
    public Item copy() {
        return new SackParcel();
    }

    @Override
    public String getSound() {
        return null;
    }

    @Override
    protected int getNotoriety() {
        return 5;
    }

    @Override
    protected Item getInnerItem() {
        if (contents == FOOD) {
            return new FoodDummyItem(amount);
        }
        if (contents == INGREDIENTS) {
            return new IngredientsDummyItem(amount);
        }
        return new MaterialsDummyItem(amount);
    }
}
