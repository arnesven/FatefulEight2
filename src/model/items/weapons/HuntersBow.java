package model.items.weapons;

import model.items.Item;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class HuntersBow extends BowWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(4, 7);

    public HuntersBow() {
        super("Hunter's Bow", 25, new int[]{8,9,11,14});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new HuntersBow();
    }
}
