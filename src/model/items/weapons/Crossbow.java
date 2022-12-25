package model.items.weapons;

import model.items.Item;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class Crossbow extends BowWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(7, 7);

    public Crossbow() {
        super("Crossbow", 24, new int[]{8,9,10,14});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Crossbow();
    }
}
