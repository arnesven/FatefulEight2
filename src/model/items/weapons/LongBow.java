package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class LongBow extends BowWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(7, 6);

    public LongBow() {
        super("Long Bow", 22, new int[]{10,10,13,13});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new LongBow();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.common;
    }
}
