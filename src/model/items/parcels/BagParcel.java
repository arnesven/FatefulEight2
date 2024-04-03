package model.items.parcels;

import model.items.IngredientsDummyItem;
import model.items.Inventory;
import model.items.Item;
import model.items.ItemDeck;
import util.MyRandom;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class BagParcel extends Parcel {

    private static final Sprite SPRITE = new ItemSprite(3, 13, MyColors.BEIGE, MyColors.BROWN, MyColors.TAN);
    private final Item potion;
    private int amount = 0;

    public BagParcel() {
        super("Bag", 2.0);
        this.potion = MyRandom.sample(ItemDeck.allPotions()).copy();
        if (MyRandom.flipCoin()) {
            this.amount = MyRandom.randInt(10, 20);
        }
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getWeight() {
        if (amount == 0) {
            return potion.getWeight();
        }
        return amount * Inventory.WEIGHT_OF_INGREDIENTS;
    }

    @Override
    public String getShoppingDetails() {
        return "A cloth bag.";
    }

    @Override
    public Item copy() {
        return new BagParcel();
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
        if (amount == 0) {
            return potion;
        }
        return new IngredientsDummyItem(amount);
    }
}
