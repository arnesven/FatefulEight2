package model.items.parcels;

import model.items.Item;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class LetterParcel extends Parcel {
    private static final Sprite SPRITE = new ItemSprite(0, 13, MyColors.BEIGE, MyColors.LIGHT_GRAY, MyColors.BEIGE);

    public LetterParcel() {
        super("Letter", 1.5);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getWeight() {
        return 0;
    }

    @Override
    public String getShoppingDetails() {
        return "An envelope.";
    }

    @Override
    public Item copy() {
        return new LetterParcel();
    }

    @Override
    public String getSound() {
        return null;
    }

    @Override
    protected int getNotoriety() {
        return 1;
    }

    @Override
    protected Item getInnerItem() {
        return null;
    }
}
