package model.items.weapons;

import model.items.Item;
import model.items.PirateItem;
import model.items.Prevalence;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Cutlass extends LongBladedWeapon implements PairableWeapon, PirateItem {
    private static final Sprite SPRITE = new ItemSprite(9, 16);

    public Cutlass() {
        super("Cutlass", 20, new int[]{6, 8, 12}, false, 0);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Cutlass();
    }

    @Override
    public Sprite makePairSprite() {
        return new ItemSprite(10, 16);
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }
}
