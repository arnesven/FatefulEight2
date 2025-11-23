package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Mace extends BluntWeapon implements PairableWeapon {
    private static final Sprite SPRITE = new ItemSprite(8, 1);

    public Mace() {
        super("Mace", 12, new int[]{5, 9, 13}, false, -1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Mace();
    }

    @Override
    public Sprite makePairSprite() {
        return new ItemSprite(0, 15);
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.common;
    }
}
