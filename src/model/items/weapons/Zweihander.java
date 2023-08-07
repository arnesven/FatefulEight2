package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class Zweihander extends BladedWeapon {

    private static final Sprite SPRITE = new TwoHandedItemSprite(12, 1);

    public Zweihander() {
        super("Zweihander", 30, new int[]{6, 8, 13, 16, 19}, true, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new TwoHandedSword();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }
}
