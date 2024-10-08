package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Choppa extends AxeWeapon implements PairableWeapon {
    private static final Sprite SPRITE = new ItemSprite(1, 11);

    public Choppa() {
        super("Choppa", 15, new int[]{5, 6, 12}, false);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Choppa();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.uncommon;
    }

    @Override
    public Sprite makePairSprite() {
        return new ItemSprite(7, 15);
    }
}
