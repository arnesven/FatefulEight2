package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class RitualDagger extends SmallBladedWeapon implements PairableWeapon {
    private static final Sprite SPRITE = new ItemSprite(5, 0);

    public RitualDagger() {
        super("Ritual Dagger", 30, new int[]{3, 4, 10}, false, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new RitualDagger();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public Sprite makePairSprite() {
        return new ItemSprite(6, 14);
    }
}
