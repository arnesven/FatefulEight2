package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.TwoHandedItemSprite;
import view.sprites.Sprite;

public class Spear extends PolearmWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(1, 4);

    public Spear() {
        super("Spear", 20, new int[]{9, 9, 9, 9});
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Spear();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.common;
    }
}
