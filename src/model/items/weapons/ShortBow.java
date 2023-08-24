package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.TwoHandedItemSprite;
import view.sprites.Sprite;

public class ShortBow extends BowWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(1, 7);

    public ShortBow() {
        super("Short Bow", 18, new int[]{7,9,11});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new ShortBow();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.common;
    }
}
