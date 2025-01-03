package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class ShortSword extends BladedWeapon implements PairableWeapon {
    private static final Sprite SPRITE = new ItemSprite(3, 0);

    public ShortSword() {
        super("Short Sword", 14, new int[]{5, 9}, false, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new ShortSword();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.common;
    }

    @Override
    public Sprite makePairSprite() {
        return new ItemSprite(4, 14);
    }
}
