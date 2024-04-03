package model.items.parcels;

import model.items.Item;
import model.items.ItemDeck;
import model.items.LockpicksDummyItem;
import model.items.ObolsDummyItem;
import util.MyRandom;
import view.MyColors;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class BoxParcel extends Parcel {
    private static final Sprite SPRITE = new ItemSprite(5, 13, MyColors.BROWN,
            MyColors.DARK_BROWN, MyColors.DARK_GRAY);
    private final int contents;
    private static final int PAPERS = 0;
    private static final int LOCK_PICKS = 1;
    private static final int OBOLS = 2;


    public BoxParcel() {
        super("Box", 2.0);
        this.contents = MyRandom.randInt(4);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public int getWeight() {
        return 1000;
    }

    @Override
    public String getShoppingDetails() {
        return "A little wooden box.";
    }

    @Override
    public Item copy() {
        return new BoxParcel();
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
        if (contents == PAPERS) {
            return null;
        }
        if (contents == LOCK_PICKS) {
            return new LockpicksDummyItem(MyRandom.randInt(3, 6));
        }
        if (contents == OBOLS) {
            return new ObolsDummyItem(MyRandom.randInt(20, 200));
        }
        return MyRandom.sample(ItemDeck.allJewelry()).copy();
    }
}
