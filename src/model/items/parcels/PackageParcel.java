package model.items.parcels;

import model.items.Item;
import model.items.ItemDeck;
import util.MyRandom;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

import java.util.ArrayList;
import java.util.List;

public class PackageParcel extends Parcel {
    private static final Sprite SPRITE = new ItemSprite(1, 13, MyColors.BEIGE, MyColors.BROWN, MyColors.PEACH);
    private final Item innerItem;

    public PackageParcel() {
        super("Package", 2.0);
        this.innerItem = makeRandomItem();
    }

    private Item makeRandomItem() {
        List<Item> items = new ArrayList<>(ItemDeck.allApparel());
        items.addAll(ItemDeck.allWeapons());
        return MyRandom.sample(items).copy();
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getWeight() {
        return innerItem.getWeight();
    }

    @Override
    public String getShoppingDetails() {
        return "A package wrapped up with string.";
    }

    @Override
    public Item copy() {
        return new PackageParcel();
    }

    @Override
    public String getSound() {
        return null;
    }
}
