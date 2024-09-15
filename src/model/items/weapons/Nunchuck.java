package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Nunchuck extends BluntWeapon implements PairableWeapon {

    private static final Sprite SPRITE = new ItemSprite(5, 11);

    public Nunchuck() {
        super("Nunchuck", 16, new int[]{7, 8, 9}, false, +1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Nunchuck();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public Sprite makePairSprite() {
        return new ItemSprite(3, 16);
    }
}
