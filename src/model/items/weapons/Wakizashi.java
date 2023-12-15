package model.items.weapons;

import model.items.Item;
import model.items.Prevalence;
import view.sprites.ItemSprite;
import view.sprites.Sprite;

public class Wakizashi extends BladedWeapon {
    private static final Sprite SPRITE = new ItemSprite(9, 11);

    public Wakizashi() {
        super("Wakizashi", 22, new int[]{8, 9, 10}, false, 1);
    }

    @Override
    protected Sprite getSprite() {
        return SPRITE;
    }

    @Override
    public Item copy() {
        return new Wakizashi();
    }

    @Override
    public int getCriticalTarget() {
        return 9;
    }

    @Override
    public Prevalence getPrevalence() {
        return Prevalence.rare;
    }
}
