package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class CompositeBow extends BowWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(2, 7);

    public CompositeBow() {
        super("Composite Bow", 18, new int[]{7, 7, 13});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new CompositeBow();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.common;
    }
}
