package model.items.weapons;

import model.items.Item;
import view.MyColors;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class BoneBow extends BowWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(6, 7, MyColors.BROWN, MyColors.LIGHT_GRAY, MyColors.BEIGE);

    public BoneBow() {
        super("Bone Bow", 25, new int[]{8,9,11,12});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new BoneBow();
    }
}
