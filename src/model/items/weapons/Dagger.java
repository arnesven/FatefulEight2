package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Dagger extends BladedWeapon {
    private static final Sprite SPRITE = new ItemSprite(1, 0);

    public Dagger() {
        super("Dagger", 8, new int[]{4, 10}, false, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Dagger();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.common;
    }
}
