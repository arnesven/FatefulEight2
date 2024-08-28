package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Broadsword extends BladedWeapon implements PairableWeapon {
    private static final Sprite SPRITE = new ItemSprite(8, 0);

    public Broadsword() {
        super("Broadsword", 16, new int[]{5, 9, 13}, false, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Broadsword();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.common;
    }

    @Override
    public Sprite makePairSprite() {
        return new ItemSprite(9, 14);
    }
}
