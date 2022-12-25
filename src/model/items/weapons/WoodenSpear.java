package model.items.weapons;

import model.items.Item;
import view.sprites.TwoHandedItemSprite;
import view.sprites.Sprite;

public class WoodenSpear extends PolearmWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(0, 4);

    public WoodenSpear() {
        super("Wooden Spear", 5, new int[]{9, 9, 9});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new WoodenSpear();
    }
}
