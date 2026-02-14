package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class Zweihander extends BladedWeapon {

    private static final Sprite SPRITE = new TwoHandedItemSprite(12, 1);

    public Zweihander() {
        super("Zweihander", 30, new int[]{6, 8, 11, 14, 14}, true, 1);
    }

//    @Override
//    public int getWeight() {
//        return 5000; // TODO:
//    }

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
