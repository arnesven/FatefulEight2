package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.Sprite;
import view.sprites.TwoHandedItemSprite;

public class Naginata extends PolearmWeapon {
    private static final Sprite SPRITE = new TwoHandedItemSprite(7, 12);

    public Naginata() {
        super("Naginata", 36, new int[]{9,9,9,10,10});
    }


    @Override
    public int getSpeedModifier() {
        return 1;
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Naginata();
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }

    @Override
    public int getCriticalTarget() {
        return 9;
    }
}
