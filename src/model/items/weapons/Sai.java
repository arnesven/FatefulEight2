package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Sai extends SmallBladedWeapon implements PairableWeapon {

    private static final Sprite SPRITE = new ItemSprite(6, 11);

    public Sai() {
        super("Sai", 18, new int[]{5, 8}, false, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Sprite makePairSprite() {
        return new ItemSprite(4, 16);
    }

    @Override
    public Item copy() {
        return new Sai();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }
}
